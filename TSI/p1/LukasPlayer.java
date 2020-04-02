package tracks.singlePlayer.advanced.p1;

import core.competition.CompetitionParameters;
import core.game.Observation;
import core.game.StateObservation;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Vector2d;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Date: 30/03/2020
 * @author Lukas Häring García
 */

public class LukasPlayer {

    public static Random randomGenerator;
    private StateObservation rootObservation;
    private Agent agent;

    Vector2d chess_size;
    Vector2d chess_pos;

    double[][] heatmap;

    ArrayList<Vector2d> planned;

    double factor = 0.f;
    double EnemyFactor = 100000.0;
    int INTEGRAL_RADIUS = 2;
    int TOTAL_GEMES = 10;
    int KERNEL_SIZE = 2;
    Random generator;

    double[][] GaussKernel = new double[][]{
        {0.00366, 0.01465, 0.02564, 0.01465, 0.00366},
        {0.01465, 0.05860, 0.09523, 0.05860, 0.01465},
        {0.02564, 0.09523, 0.15018, 0.09523, 0.02564},
        {0.01465, 0.05860, 0.09523, 0.05860, 0.01465},
        {0.00366, 0.01465, 0.02564, 0.01465, 0.00366}
    };

    private boolean INIT_ENEMIES;
    private boolean INIT_GEMES;

    public LukasPlayer(StateObservation gameState, Agent _agent) {
        factor = 1. / gameState.getBlockSize();
        chess_size = new Vector2d(
            Math.round(gameState.getWorldDimension().width * factor),
            Math.round(gameState.getWorldDimension().height * factor)
        );
        INIT_ENEMIES = gameState.getNPCPositions() != null;
        INIT_GEMES = gameState.getResourcesPositions() != null;
        agent = _agent;
        generator = new Random(2);
    }

    private Double metric(Vector2d a, Vector2d b){
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    };

    public void init(StateObservation gameState) {

        ArrayList<Observation>[][] grid = gameState.getObservationGrid();

        chess_pos = gameState.getAvatarPosition().mul(factor);

        heatmap = new double[(int)chess_size.y][(int)chess_size.x];

        // Heatmap for the enemies
        ArrayList<Observation>[] entities = gameState.getNPCPositions();
        if(entities != null) {
            ArrayList<Observation> enemies = entities[0];
            for (Observation enemy : enemies) {

                int x = (int)(enemy.position.x * factor);
                int y = (int)(enemy.position.y * factor);

                for (int j = -KERNEL_SIZE; j <= KERNEL_SIZE; ++j) {
                    for (int i = -KERNEL_SIZE; i <= KERNEL_SIZE; ++i) {
                        if(line_transitable(x + i, y + j, x, y, grid)) {
                            heatmap[y + j][x + i] += GaussKernel[j + KERNEL_SIZE][i + KERNEL_SIZE] * EnemyFactor;
                        }
                    }
                }
            }
        }
    }

    public Types.ACTIONS run(StateObservation gameState, ElapsedCpuTimer elapsedTimer) {

        init(gameState);

        ArrayList<Observation>[][] grid = gameState.getObservationGrid();
        ArrayList<Observation>[] resources = gameState.getResourcesPositions();

        if(INIT_ENEMIES && !INIT_GEMES && gameState.getGameTick() < 2000){ // Level 3/4

            PriorityQueue<A_Node> posible = new PriorityQueue<>();

            int x = (int) chess_pos.x;
            int y = (int) chess_pos.y;
            for(int j = -INTEGRAL_RADIUS; j <= INTEGRAL_RADIUS; ++j){
                for(int i = -INTEGRAL_RADIUS; i <= INTEGRAL_RADIUS; ++i){
                    if(i != 0 && j != 0 && transitable(x + i, y + j, grid)) {
                        A_Node neighbour = a_star(gameState, chess_pos, new Vector2d(x + i, y + j), grid, elapsedTimer);
                        if (neighbour != null) {
                            neighbour.fcal += line_transitable(x + i * 2, y + j * 2, x + i, y + j, grid) ? 0.0 : EnemyFactor;
                            posible.add(neighbour);
                        }
                    }
                }
            }

            return backtracking_action(gameState, posible.poll());

        }else if(resources == null || gameState.getGameScore() >= 2.0 * TOTAL_GEMES) {
            Vector2d door = gameState.getPortalsPositions()[0].get(0).position.mul(factor);
            return backtracking_action(gameState, a_star(gameState, chess_pos, door, grid, elapsedTimer));
        }else{
            ArrayList<Observation> gemes = resources[0];

            ArrayList<Vector2d> positions = new ArrayList<Vector2d>(gemes.stream().map(observation -> observation.position.copy().mul(factor)).collect(Collectors.toList()));
            // sort by nearest if we dont have enough time
            positions.sort((a, b) -> (int)(a.mag() - b.mag()));

            A_Node best = a_star(gameState, chess_pos, positions.get(0), grid, elapsedTimer);
            double best_f = best == null ? 0.0 : best.fcal;

            int s = positions.size();
            for (int j = 1; j < s && !elapsedTimer.exceededMaxTime(); ++j){
                A_Node next = a_star(gameState, chess_pos, positions.get(j), grid, elapsedTimer);
                double path_f = next.fcal;
                if(path_f < best_f){
                    best = next;
                    best_f = path_f;
                }
            }

            return backtracking_action(gameState, best);
        }
    }

    public double discrete_integral(int x, int y, int r, ArrayList<Observation>[][] grid){
        double ht = 0.0;
        for (int j = -r; j <= r; ++j) {
            for (int i = -r; i <= r; ++i) {
                if(line_transitable(x + i, y + j, x, y, grid)) {
                    ht += heatmap[y + j][x + i];
                }
            }
        }
        return ht;
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
            this.h = metric(pos, goal) + discrete_integral((int) pos.x, (int) pos.y, INTEGRAL_RADIUS, grid);
        };

        private void g(){
            this.g = this.from.g + 1.0 + (compare(this.lookat, this.from.lookat) ? 0.0 : 1.0);
        };

        @Override
        public int compareTo(A_Node o) {
            return (int)Math.signum(fcal - o.fcal);
        }
    };

    private boolean transitable(int x, int y, ArrayList<Observation>[][] grid){
        if(x < 0 || x >= chess_size.x || y < 0 || y >= chess_size.y) return false;
        ArrayList<Observation> cell = grid[x][y];
        return (
            cell.size() == 0 || (
                cell.size() > 0 && !(cell.get(0).category == 4)
            )
        );
    };

    // Bresenham's line algorithm https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
    private boolean line_transitable(int x0, int y0, int x1, int y1, ArrayList<Observation>[][] grid){

        double dx = Math.abs(x1 - x0);
        double dy = -Math.abs(y1 - y0);
        double sx = Math.signum(x1 - x0);
        double sy = Math.signum(y1 - y0);
        double err = dx + dy;
        while (true) {

            if (!transitable(x0, y0, grid)) {
                return false;
            }

            if (x0 == x1 && y0 == y1) break;

            double e2 = 2 * err;
            if (e2 >= dy) {
                err += dy;
                x0 += sx;
            }
            if (e2 <= dx) {
                err += dx;
                y0 += sy;
            }
        }
        return true;

    }

    private boolean compare(Vector2d a, Vector2d b){
        return a.copy().subtract(b).mag() < 0.001;
    };

    private A_Node a_star(StateObservation gameState, Vector2d from, Vector2d to, ArrayList<Observation>[][] grid, ElapsedCpuTimer elapsedTimer) {

        Types.ACTIONS action = Types.ACTIONS.ACTION_NIL;

        PriorityQueue<A_Node> open = new PriorityQueue<>();
        A_Node a_from = new A_Node(null, from, gameState.getAvatarOrientation());
        open.add(a_from);

        A_Node[][] visited = new A_Node[(int) chess_size.y][(int) chess_size.x];
        visited[(int) from.y][(int) from.x] = a_from;

        while (!open.isEmpty()) {
            A_Node expanded = open.poll();
            if (compare(expanded.pos, to)) {
                return expanded;
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

        return null;
    }

    Types.ACTIONS backtracking_action(StateObservation gameState, A_Node path){
        if(path == null || path.from == null) return Types.ACTIONS.ACTION_NIL;

        // Backtracking
        while (path.from.from != null) {
            path = path.from;
        }

        Vector2d vfrom = path.from.pos.subtract(path.pos).unitVector();
        int idx = (int) (vfrom.theta() * 2.0 / Math.PI) + 1;
        return MOVEMENT[idx];
    };

    static Types.ACTIONS[] MOVEMENT = new Types.ACTIONS[]{
        Types.ACTIONS.ACTION_DOWN,
        Types.ACTIONS.ACTION_LEFT,
        Types.ACTIONS.ACTION_UP,
        Types.ACTIONS.ACTION_RIGHT
    };

}
