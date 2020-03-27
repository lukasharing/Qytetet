package tracks.singlePlayer.advanced.lukas;

import core.game.Observation;
import core.game.StateObservation;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Vector2d;

import javax.swing.*;
import javax.xml.transform.stream.StreamSource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Code written by Adrien Couetoux, acouetoux@ulg.ac.be.
 * Date: 15/12/2015
 * @author Adrien Couëtoux
 */

public class LukasPlayer {

    public static Random randomGenerator;
    private StateObservation rootObservation;
    private Agent agent;

    Vector2d chess_size;
    Vector2d chess_pos;

    double[][] heatmap;

    float factor = 0.f;
    static double GemeFactor = 100.0;
    static double EnemyFactor = 10000.0;
    static double[][] GaussKernel = new double[][]{
        {0.00366, 0.01465, 0.02564, 0.01465, 0.00366},
        {0.01465, 0.05860, 0.09523, 0.05860, 0.01465},
        {0.02564, 0.09523, 0.15018, 0.09523, 0.02564},
        {0.01465, 0.05860, 0.09523, 0.05860, 0.01465},
        {0.00366, 0.01465, 0.02564, 0.01465, 0.00366}
    };

    public LukasPlayer(StateObservation gameState, Agent agent) {
        factor = (float)(1. / gameState.getBlockSize());
        chess_size = new Vector2d(
            gameState.getWorldDimension().width * factor,
            gameState.getWorldDimension().height * factor
        );

        this.agent = agent;
    }

    private Double metric(Vector2d a, Vector2d b){
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    };

    public void init(StateObservation gameState) {

        chess_pos = gameState.getAvatarPosition().mul(1./ gameState.getBlockSize());
        heatmap = new double[(int)chess_size.y + 4][(int)chess_size.x + 4];

        // Heatmap for the resources
        ArrayList<Observation>[] resources = gameState.getResourcesPositions();
        if(resources != null) {
            ArrayList<Observation> gemes = resources[0];
            for (Observation gem : gemes) {

                int x = (int) (gem.position.x * factor) + 2;
                int y = (int) (gem.position.y * factor) + 2;

                for (int j = -2; j <= 2; ++j) {
                    for (int i = -2; i <= 2; ++i) {
                        heatmap[y + j][x + i] -= GaussKernel[j + 2][i + 2] * GemeFactor;
                    }
                }
            }
        }

        // Heatmap for the enemies
        ArrayList<Observation>[] entities = gameState.getNPCPositions();
        if(entities != null) {
            ArrayList<Observation> enemies = entities[0];
            for (Observation enemy : enemies) {

                int x = (int) (enemy.position.x * factor) + 2;
                int y = (int) (enemy.position.y * factor) + 2;

                for (int j = -2; j <= 2; ++j) {
                    for (int i = -2; i <= 2; ++i) {
                        heatmap[y + j][x + i] += GaussKernel[j + 2][i + 2] * EnemyFactor;
                    }
                }
            }
        }

    }

    public Types.ACTIONS run(StateObservation gameState, ElapsedCpuTimer elapsedTimer) {

        init(gameState);

        Types.ACTIONS action = Types.ACTIONS.ACTION_NIL;

        // There are still gemes
        ArrayList<Observation>[] resources = gameState.getResourcesPositions();

        if(resources != null && gameState.getGameScore() <= 9 * 2){
            ArrayList<Observation> gemes = resources[0];

            ArrayList<Vector2d> gemes_position = new ArrayList<Vector2d>(gemes.stream().map(observation -> observation.position.copy().mul(factor)).collect(Collectors.toList()));
            gemes_position.sort((observation, t1) -> (int)(
                metric(observation, chess_pos) - metric(t1, chess_pos)
            ));

            for(int i = 0; i < gemes_position.size() && action == Types.ACTIONS.ACTION_NIL && elapsedTimer.elapsedMillis() < 40.0; ++i){
                action = a_star(gameState, gemes_position.get(i), elapsedTimer);
            }

        }else{
            Vector2d to = gameState.getPortalsPositions()[0].get(0).position.mul(factor);
            action = a_star(gameState, to, elapsedTimer);
        }

        return action;
    }


    class A_Node implements Comparable<A_Node>{
        A_Node from;
        Vector2d pos;
        double h;
        double g;
        double f;

        int x(){ return (int)pos.x; }
        int y(){ return (int)pos.y; }

        A_Node(A_Node r, Vector2d p, double h, double g){
            this.from = r;
            this.pos  = p;
            this.h    = h;
            this.g    = g;
            this.f    = g + h;
        }

        @Override
        public String toString() {
            return "A_Node{" +
                    " pos=" + pos +
                    ", g=" + g +
                    ", f=" + f +
                    '}';
        }

        @Override
        public int compareTo(A_Node o) {
            return f < o.f ? -1 : f > o.f ? 1 : 0;
        }
    };

    private boolean transitable(ArrayList<Observation> cell){
        return (
                cell.size() == 0 || (
                    cell.size() > 0 && !(
                        cell.get(0).category == 4 ||
                        cell.get(0).category == 3
                    )
                )
        );
    }

    private double g(A_Node expanded, Vector2d to, Vector2d initial_orientation){

        Vector2d orientation = initial_orientation;
        // If it comes from another node, the orientation is not the initial
        if(expanded.from != null){
            orientation = expanded.pos.copy().subtract(expanded.from.pos);
        }

        // Direction
        Vector2d dir = to.copy().subtract(expanded.pos);
        double dr = Math.min(1.0, Math.abs(dir.theta() - orientation.theta())  * 2.0 / Math.PI);

        return expanded.g + 1.0 + dr;
    };

    private Function<Pair<Vector2d, Vector2d>, Double> h = v -> {

        // Heatmap
        Double ht = 0.0;

        int c = 2;
        for(int j = -c; j <= c; ++j){
            for(int i = -c; i <= c; ++i){
                ht += heatmap[(int)from.y + 2 + j][(int)from.x + 2 + i];
            }
        }

        return metric(v, goal) + ht;
    };

    private boolean compare(Vector2d a, Vector2d b){
        return a.copy().subtract(b).mag() < 0.001;
    }

    private Types.ACTIONS a_star(StateObservation gameState, Vector2d to, Function<Vector2d, Vector2d, double> h, ElapsedCpuTimer elapsedTimer) {
        Types.ACTIONS action = Types.ACTIONS.ACTION_NIL;
        ArrayList<Observation>[][] grid = gameState.getObservationGrid();

        if (compare(chess_pos, to)) {
            ArrayList<Observation> cell = grid[(int) chess_pos.x][(int) chess_pos.y];
            if (cell.size() == 2 && cell.get(1).category == 2) {
                action = Types.ACTIONS.ACTION_ESCAPE;
            }
            return action;
        }

        PriorityQueue<A_Node> open = new PriorityQueue<>();
        A_Node a_from = new A_Node(null, chess_pos, 0, 0);
        open.add(a_from);

        A_Node[][] visited = new A_Node[(int) chess_size.y][(int) chess_size.x];
        visited[(int) chess_pos.y][(int) chess_pos.x] = a_from;

        A_Node solution = null;
        while (!open.isEmpty()) {

            A_Node expanded = open.poll();
            if (compare(expanded.pos, to)) {
                solution = expanded;
                break;
            }

            int x = expanded.x();
            int y = expanded.y();
            Vector2d orientation = gameState.getAvatarOrientation();

            // Vertical
            // TOP
            ArrayList<Observation> top = grid[x][y - 1];
            if(transitable(top)){
                A_Node a_top = visited[y - 1][x];
                Vector2d ct = new Vector2d(x, y - 1);
                A_Node child = new A_Node(expanded, ct, h(ct, to), g(expanded, ct, orientation));
                if (a_top == null || (a_top != null && child.f < a_top.f)) {
                    open.add(child);
                    visited[y - 1][x] = child;
                }
            }
            // BOTTOM
            ArrayList<Observation> bottom = grid[x][y + 1];
            if(transitable(bottom)) {
                A_Node a_top = visited[y + 1][x];
                Vector2d cb = new Vector2d(x, y + 1);
                A_Node child = new A_Node(expanded, cb, h(cb, to), g(expanded, cb, orientation));
                if (a_top == null || (a_top != null && child.f < a_top.f)) {
                    open.add(child);
                    visited[y + 1][x] = child;
                }
            }
            // Horizontal
            // LEFT
            ArrayList<Observation> left = grid[x - 1][y];
            if(transitable(left)){
                A_Node a_top = visited[y][x - 1];
                Vector2d cl = new Vector2d(x - 1, y);
                A_Node child = new A_Node(expanded, cl, h(cl, to), g(expanded, cl, orientation));
                if (a_top == null || (a_top != null && child.f < a_top.f)) {
                    open.add(child);
                    visited[y][x - 1] = child;
                }
            }
            // RIGHT
            ArrayList<Observation> right = grid[x + 1][y];
            if(transitable(right)) {
                A_Node a_top = visited[y][x + 1];
                Vector2d cr = new Vector2d(x + 1, y);
                A_Node child = new A_Node(expanded, cr, h(cr, to), g(expanded, cr, orientation));
                if (a_top == null || (a_top != null && child.f < a_top.f)) {
                    open.add(child);
                    visited[y][x + 1] = child;
                }
            }
        }

        if (solution != null) {
            // Backtracking
            while (solution.from.from != null) {
                solution = solution.from;
            }

            Vector2d vfrom = solution.from.pos.subtract(solution.pos).unitVector();
            int idx = (int)(vfrom.theta() * 2.0 / Math.PI) + 1;
            action = MOVEMENT[idx];
        }

        return action;
    }

    static Types.ACTIONS[] MOVEMENT = new Types.ACTIONS[]{
        Types.ACTIONS.ACTION_DOWN,
        Types.ACTIONS.ACTION_LEFT,
        Types.ACTIONS.ACTION_UP,
        Types.ACTIONS.ACTION_RIGHT
    };

}
