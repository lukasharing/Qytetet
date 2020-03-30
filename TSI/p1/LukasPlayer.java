package tracks.singlePlayer.advanced.p1;

import core.game.Observation;
import core.game.StateObservation;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Vector2d;

import javax.swing.*;
import javax.xml.transform.stream.StreamSource;
import java.lang.reflect.Array;
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

    ArrayList<Vector2d> planned;

    float factor = 0.f;
    static double GemeFactor = 1.0;
    static double EnemyFactor = 1000.0;

    static int KERNEL_SIZE = 2;
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

        ArrayList<Observation>[][] grid = gameState.getObservationGrid();

        chess_pos = gameState.getAvatarPosition().mul(1./ gameState.getBlockSize());
        heatmap = new double[(int)chess_size.y + 2 * KERNEL_SIZE][(int)chess_size.x + 2 * KERNEL_SIZE];

        // Heatmap for the resources
        ArrayList<Observation>[] resources = gameState.getResourcesPositions();
        if(resources != null) {
            ArrayList<Observation> gemes = resources[0];
            for (Observation gem : gemes) {
                int x = (int) (gem.position.x * factor);
                int y = (int) (gem.position.y * factor);

                for (int j = 0; j <= 2 * KERNEL_SIZE; ++j) {
                    for (int i = 0; i <= 2 * KERNEL_SIZE; ++i) {
                        if(!transitable(x + i, y + j, grid)) break;
                        heatmap[y + j][x + i] -= GaussKernel[j][i] * GemeFactor;
                    }
                }
            }
        }

        // Heatmap for the enemies
        ArrayList<Observation>[] entities = gameState.getNPCPositions();
        if(entities != null) {
            ArrayList<Observation> enemies = entities[0];
            for (Observation enemy : enemies) {

                int x = (int) (enemy.position.x * factor);
                int y = (int) (enemy.position.y * factor);

                for (int j = 0; j <= 2 * KERNEL_SIZE; ++j) {
                    for (int i = 0; i <= 2 * KERNEL_SIZE; ++i) {
                        if(!transitable(x + i, y + j, grid)) break;
                        heatmap[y + j][x + i] += GaussKernel[j][i] * EnemyFactor;
                    }
                }
            }
        }
    }

    public Types.ACTIONS run(StateObservation gameState, ElapsedCpuTimer elapsedTimer) {

        init(gameState);

        ArrayList<Observation>[] resources = gameState.getResourcesPositions();
        A_Node best = null;
        if(resources == null || (resources != null && resources[0].size() == 0) || gameState.getGameScore() >= 2.0 * 10.) {
            Vector2d door = gameState.getPortalsPositions()[0].get(0).position.mul(factor);
            best = a_star(gameState, chess_pos, door, 0L);
        }else{
            ArrayList<Observation> gemes = resources[0];

            ArrayList<Vector2d> positions = new ArrayList<Vector2d>(gemes.stream().map(observation -> observation.position.copy().mul(factor)).collect(Collectors.toList()));
            int s = positions.size();
            best = a_star(gameState, chess_pos, positions.get(0), 0L);
            int best_path_length = backtracking_count(best);

            for (int j = 1; j < s; ++j) {
                A_Node next = a_star(gameState, chess_pos, positions.get(j), 0L);
                int path_length = backtracking_count(next);
                if(path_length < best_path_length){
                    best_path_length = path_length;
                    best = next;
                }
            }
        }

        Types.ACTIONS action = backtracking_action(gameState, best);


        return action;
    }


    class A_Node implements Comparable<A_Node>{
        A_Node from;
        Vector2d pos;
        Vector2d lookat;
        double h;
        double g;
        public double fcal;

        int x(){ return (int)pos.x; }
        int y(){ return (int)pos.y; }

        A_Node(A_Node r, Vector2d p, Vector2d l){
            this.from = r;
            this.pos  = p;
            this.lookat = l;
            this.h = 0.0;
            this.g = 0.0;
            this.fcal = 0.0;
        }

        public void f(Vector2d goal, ArrayList<Observation>[][] grid){
            h(goal, grid);
            g();
            this.fcal = h + g;
        };

        public void h(Vector2d goal, ArrayList<Observation>[][] grid){
            // Heatmap
            Double ht = 0.0;

            // Integral
            int y = (int) pos.y;
            int x = (int) pos.x;
            double m = 0.0;
            for (int j = 0; j <= 2 * KERNEL_SIZE; ++j) {
                for (int i = 0; i <= 2 * KERNEL_SIZE; ++i) {
                    if(!transitable(x + i, y + j, grid)) break;
                    ht += heatmap[y + j][x + i];
                    ++m;
                }
            }
            ht /= m;

            this.h = metric(pos, goal) + ht;
        };

        private void g(){
            this.g = this.from.g + 1.0 + (compare(this.lookat, this.from.lookat) ? 0.0 : 1.0);
        };

        @Override
        public String toString() {
            return "A_Node{" +
                " pos=" + pos +
                ", g=" + g +
                ", f=" + fcal +
            '}';
        };

        @Override
        public int compareTo(A_Node o) {
            return (int)Math.signum(fcal - o.fcal);
        }
    };

    private boolean transitable(int x, int y, ArrayList<Observation>[][] grid){
        if(x < 0 || x >= (int)chess_size.x || y < 0 || y >= (int)chess_size.y) return false;
        ArrayList<Observation> cell = grid[x][y];
        return (
            cell.size() == 0 || (
                cell.size() > 0 && !(
                    cell.get(0).category == 4 ||
                    cell.get(0).category == 3
                )
            )
        );
    };

    private boolean compare(Vector2d a, Vector2d b){
        return a.copy().subtract(b).mag() < 0.001;
    };

    private A_Node a_star(StateObservation gameState, Vector2d from, Vector2d to, long elapsed) {
        Types.ACTIONS action = Types.ACTIONS.ACTION_NIL;
        ArrayList<Observation>[][] grid = gameState.getObservationGrid();

        PriorityQueue<A_Node> open = new PriorityQueue<>();
        A_Node a_from = new A_Node(null, from, gameState.getAvatarOrientation());
        open.add(a_from);

        A_Node[][] visited = new A_Node[(int) chess_size.y][(int) chess_size.x];
        visited[(int) from.y][(int) from.x] = a_from;

        A_Node solution = null;
        while (!open.isEmpty()) {
            A_Node expanded = open.poll();
            if (compare(expanded.pos, to)) {
                solution = expanded;
                break;
            }

            // Coords
            int x = expanded.x(), y = expanded.y();

            // Vertical
            // TOP
            if(transitable(x, y - 1, grid)){
                A_Node a_top = visited[y - 1][x];
                A_Node child = new A_Node(expanded, new Vector2d(x, y - 1), new Vector2d(+0.0, -1.0));
                child.f(to, grid);

                if (a_top == null || (a_top != null && child.g <= a_top.g)) {
                    open.add(child);
                    visited[y - 1][x] = child;
                }
            }
            // BOTTOM
            if(transitable(x, y + 1, grid)) {

                A_Node a_top = visited[y + 1][x];
                A_Node child = new A_Node(expanded, new Vector2d(x, y + 1), new Vector2d(+0.0, +1.0));
                child.f(to, grid);

                if (a_top == null || (a_top != null && child.g <= a_top.g)) {
                    open.add(child);
                    visited[y + 1][x] = child;
                }
            }
            // Horizontal
            // LEFT
            if(transitable(x - 1, y, grid)){
                A_Node a_top = visited[y][x - 1];
                A_Node child = new A_Node(expanded, new Vector2d(x - 1, y), new Vector2d(-1.0, +0.0));
                child.f(to, grid);

                if (a_top == null || (a_top != null && child.g <= a_top.g)) {
                    open.add(child);
                    visited[y][x - 1] = child;
                }
            }
            // RIGHT
            if(transitable(x + 1, y, grid)) {
                A_Node a_top = visited[y][x + 1];
                A_Node child = new A_Node(expanded, new Vector2d(x + 1, y), new Vector2d(+1.0, +0.0));
                child.f(to, grid);

                if (a_top == null || (a_top != null && child.g <= a_top.g)) {
                    open.add(child);
                    visited[y][x + 1] = child;
                }
            }
        }

        return solution;
    }

    Types.ACTIONS backtracking_action(StateObservation gameState, A_Node path){
        if(path == null) return Types.ACTIONS.ACTION_NIL;

        // We are hover something
        if(path.from == null){
            ArrayList<Observation> current = gameState.getObservationGrid()[(int) path.pos.x][(int) path.pos.y];

            // It is the way out!
            if (current.size() == 2 && current.get(1).category == 2) {
                return Types.ACTIONS.ACTION_ESCAPE;
            }

            return Types.ACTIONS.ACTION_NIL;
        }

        // Backtracking
        while (path.from.from != null) {
            path = path.from;
        }

        Vector2d vfrom = path.from.pos.subtract(path.pos).unitVector();
        int idx = (int) (vfrom.theta() * 2.0 / Math.PI) + 1;
        return MOVEMENT[idx];
    };

    int backtracking_count(A_Node path){
        if(path == null) return 0;

        int total = 0;
        while (path.from != null) {
            path = path.from;
            ++total;
        }
        return total;
    };


    static Types.ACTIONS[] MOVEMENT = new Types.ACTIONS[]{
        Types.ACTIONS.ACTION_DOWN,
        Types.ACTIONS.ACTION_LEFT,
        Types.ACTIONS.ACTION_UP,
        Types.ACTIONS.ACTION_RIGHT
    };

}
