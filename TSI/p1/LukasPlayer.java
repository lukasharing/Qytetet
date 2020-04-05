package tracks.singlePlayer.advanced.p1;

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

    private Agent agent;

    Vector2d chess_size;
    Vector2d chess_pos;
    ArrayList<Vector2d> gemes_position;
    ArrayList<Observation>[][] grid;


    double[][] heatmap;

    ArrayList<Integer> best_path;

    double factor = 0.f;
    double EnemyFactor = 10000.0;
    int INTEGRAL_RADIUS = 2;
    int TOTAL_GEMES = 10;
    int KERNEL_SIZE = 2;
    double last_points = 0.0;

    double[][] GaussKernel = new double[][]{
        {0.00366, 0.01465, 0.02564, 0.01465, 0.00366},
        {0.01465, 0.05860, 0.09523, 0.05860, 0.01465},
        {0.02564, 0.09523, 0.15018, 0.09523, 0.02564},
        {0.01465, 0.05860, 0.09523, 0.05860, 0.01465},
        {0.00366, 0.01465, 0.02564, 0.01465, 0.00366}
    };

    private boolean INIT_ENEMIES;
    private boolean INIT_GEMES;

    class BranchAndBound implements Comparable<BranchAndBound>{

        BranchAndBound parent;
        int depth;
        int value;
        double f;

        boolean isFinal(){ return this.depth == TOTAL_GEMES; };


        BranchAndBound(BranchAndBound parent, int next, double[] start_to, double[][] distance_mtx, double[] end_to){
            this.depth = parent == null ? 1 : parent.depth + 1;
            this.parent = parent;
            this.value = next;

            if(parent == null){
                this.f = start_to[next];
            }else{
                this.f = this.parent.f + distance_mtx[this.parent.value][next];

                if(isFinal()){
                    this.f += end_to[next];
                }
            }
        };

        boolean used(int i){
            BranchAndBound next = this;
            boolean result = false;
            while(next != null){ result |= next.value == i; next = next.parent; }
            return result;
        };

        public ArrayList<Integer> toArrayList(){
            ArrayList<Integer> result = new ArrayList<>();
            result.add(0, this.value);
            BranchAndBound it = this.parent;
            while(it != null){
                result.add(0, it.value);
                it = it.parent;
            }
            return result;
        };

        @Override
        public int compareTo(BranchAndBound o) {
            return (int)(o.depth - depth);
        };
    }


    public LukasPlayer(StateObservation gameState, Agent _agent, ElapsedCpuTimer elapsedTimer) {
        factor = 1. / gameState.getBlockSize();
        chess_size = new Vector2d(
            Math.round(gameState.getWorldDimension().width * factor),
            Math.round(gameState.getWorldDimension().height * factor)
        );

        grid = gameState.getObservationGrid();
        chess_pos = gameState.getAvatarPosition().mul(factor);
        INIT_ENEMIES = gameState.getNPCPositions() != null;

        INIT_GEMES = gameState.getResourcesPositions() != null;

        agent = _agent;
        heatmap = new double[(int)chess_size.y][(int)chess_size.x];

        // Local Search
        if(INIT_GEMES) {
            gemes_position = new ArrayList<Vector2d>(gameState.getResourcesPositions()[0].stream().map(observation -> observation.position.copy().mul(factor)).collect(Collectors.toList()));

            // Distance matrix
            int s = gemes_position.size();
            double[] from_fvalues = new double[s];
            double[][] fvalues = new double[s][s];
            double[] to_fvalues = new double[s];
            Vector2d door = gameState.getPortalsPositions()[0].get(0).position.mul(factor);

            for(int i = 0; i < s; ++i){
                A_Node from_pos = a_star(gameState, chess_pos, gemes_position.get(i), false, null);
                A_Node to_door = a_star(gameState, gemes_position.get(i), door, false, null);
                from_fvalues[i] = from_pos != null && from_pos.fcal != 0.0 ? from_pos.fcal : 10e10;
                to_fvalues[i] = to_door != null && to_door.fcal != 0.0 ? to_door.fcal : 10e10;
                for(int j = i; j < s; ++j){
                    A_Node path = a_star(gameState, gemes_position.get(i), gemes_position.get(j), false, null);
                    double distance = path != null && path.fcal != 0.0 ? path.fcal : 10e10;
                    fvalues[i][j] = distance;
                    fvalues[j][i] = distance;
                }
            }

            PriorityQueue<BranchAndBound> posible = new PriorityQueue<>();
            for(int i = 0; i < gemes_position.size(); ++i){
                posible.add(new BranchAndBound(null, i, from_fvalues, fvalues, to_fvalues));
            }

            double upper = 10e5;
            BranchAndBound best = null;
            while (!posible.isEmpty()){
                BranchAndBound node = posible.poll();

                if(elapsedTimer.exceededMaxTime()){ break; }

                if(node.f < upper){
                    if(node.isFinal()) {
                        upper = node.f;
                        best = node;
                    }else{
                        for (int i = 0; i < gemes_position.size(); ++i) {
                            if(!node.used(i)) {
                                posible.add(new BranchAndBound(node, i, from_fvalues, fvalues, to_fvalues));
                            }
                        }
                    }
                }
            }

            best_path = best.toArrayList();
        }
    }



    private Double metric(Vector2d a, Vector2d b){
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    };

    public void init(StateObservation gameState) {

        chess_pos = gameState.getAvatarPosition().mul(factor);
        grid = gameState.getObservationGrid();
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
                        if(line_transitable(x + i, y + j, x, y)) {
                            heatmap[y + j][x + i] += GaussKernel[j + KERNEL_SIZE][i + KERNEL_SIZE] * EnemyFactor;
                        }
                    }
                }
            }
        }
    }

    public Types.ACTIONS run(StateObservation gameState, ElapsedCpuTimer elapsedTimer) {

        init(gameState);

        grid = gameState.getObservationGrid();
        ArrayList<Observation>[] resources = gameState.getResourcesPositions();

        int x = (int) chess_pos.x;
        int y = (int) chess_pos.y;

        // Remove geme if score has changed
        if(last_points < gameState.getGameScore()){
            last_points = gameState.getGameScore();
            for(int i = 0; i < best_path.size(); ++i){
                Vector2d position = gemes_position.get(best_path.get(i));
                ArrayList<Observation> list = grid[(int)position.x][(int)position.y];
                if(list.size() == 1 && list.get(0).category == 0){
                    best_path.remove(i);
                    break;
                }
            }
        }


        if(INIT_ENEMIES && !INIT_GEMES){ // Level 3/4

            PriorityQueue<A_Node> possible = new PriorityQueue<>();

            for(int j = -(INTEGRAL_RADIUS + KERNEL_SIZE); j <= (INTEGRAL_RADIUS + KERNEL_SIZE); ++j){
                for(int i = -(INTEGRAL_RADIUS + KERNEL_SIZE); i <= (INTEGRAL_RADIUS + KERNEL_SIZE); ++i){
                    if(i != 0 && j != 0 && transitable(x + i, y + j)) {
                        A_Node neighbour = a_star(gameState, chess_pos, new Vector2d(x + i, y + j), true, elapsedTimer);
                        if (neighbour != null) {
                            possible.add(neighbour);
                        }
                    }
                }
            }

            return backtracking_action(gameState, possible.poll());

        }else if(best_path == null || best_path.isEmpty() || gameState.getGameScore() >= 2.0 * TOTAL_GEMES) {
            Vector2d door = gameState.getPortalsPositions()[0].get(0).position.mul(factor);
            return backtracking_action(gameState, a_star(gameState, chess_pos, door, true, elapsedTimer));
        }else{
            // From the planification given, re-planify if needed
            A_Node best = a_star(gameState, chess_pos, gemes_position.get(best_path.get(0)), true, elapsedTimer);
            if(best_path.size() > 1) {
                A_Node best_next = a_star(gameState, chess_pos, gemes_position.get(best_path.get(1)), true, elapsedTimer);
                if (best_next.fcal < best.fcal) {
                    best = best_next;
                    Collections.swap(best_path, 0, 1);
                }
            }
            return backtracking_action(gameState, best);
        }
    }

    public double discrete_integral(int x, int y, int r){
        double ht = 0.0;
        for (int j = -r; j <= r; ++j) {
            for (int i = -r; i <= r; ++i) {
                if(line_transitable(x + i, y + j, x, y)) {
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

        public void f(Vector2d goal, boolean heuristic){
            if(heuristic){ h(goal); }
            g();
            this.fcal = h + g;
        };

        public void h(Vector2d goal){
            this.h = metric(pos, goal) + discrete_integral((int) pos.x, (int) pos.y, INTEGRAL_RADIUS);
        };

        private void g(){
            this.g = this.from.g + 1.0 + (compare(this.lookat, this.from.lookat) ? 0.0 : 1.0);
        };

        @Override
        public int compareTo(A_Node o) {
            return (int)Math.signum(fcal - o.fcal);
        }
    };

    private boolean transitable(int x, int y){
        if(x < 0 || x >= chess_size.x || y < 0 || y >= chess_size.y) return false;
        ArrayList<Observation> cell = grid[x][y];
        return (
            cell.size() == 0 || (
                cell.size() > 0 && !(cell.get(0).category == 4)
            )
        );
    };

    // Bresenham's line algorithm https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
    private boolean line_transitable(int x0, int y0, int x1, int y1){

        double dx = Math.abs(x1 - x0);
        double dy = -Math.abs(y1 - y0);
        double sx = Math.signum(x1 - x0);
        double sy = Math.signum(y1 - y0);
        double err = dx + dy;
        while (true) {

            if (!transitable(x0, y0)) {
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

    private A_Node a_star(StateObservation gameState, Vector2d from, Vector2d to, boolean heuristic, ElapsedCpuTimer elapsedTimer) {

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
            if(transitable(x, y - 1)){
                A_Node a_top = visited[y - 1][x];
                A_Node child = new A_Node(expanded, new Vector2d(x, y - 1), new Vector2d(+0.0, -1.0));
                child.f(to, heuristic);

                if (a_top == null || (a_top != null && child.g <= a_top.g)) {
                    open.add(child);
                    visited[y - 1][x] = child;
                }
            }
            // BOTTOM
            if(transitable(x, y + 1)) {

                A_Node a_top = visited[y + 1][x];
                A_Node child = new A_Node(expanded, new Vector2d(x, y + 1), new Vector2d(+0.0, +1.0));
                child.f(to, heuristic);

                if (a_top == null || (a_top != null && child.g <= a_top.g)) {
                    open.add(child);
                    visited[y + 1][x] = child;
                }
            }
            // Horizontal
            // LEFT
            if(transitable(x - 1, y)){
                A_Node a_top = visited[y][x - 1];
                A_Node child = new A_Node(expanded, new Vector2d(x - 1, y), new Vector2d(-1.0, +0.0));
                child.f(to, heuristic);

                if (a_top == null || (a_top != null && child.g <= a_top.g)) {
                    open.add(child);
                    visited[y][x - 1] = child;
                }
            }
            // RIGHT
            if(transitable(x + 1, y)) {
                A_Node a_top = visited[y][x + 1];
                A_Node child = new A_Node(expanded, new Vector2d(x + 1, y), new Vector2d(+1.0, +0.0));
                child.f(to, heuristic);

                if (a_top == null || (a_top != null && child.g <= a_top.g)) {
                    open.add(child);
                    visited[y][x + 1] = child;
                }
            }

            // If Maxtime Elapsed, return null
            if(elapsedTimer != null && elapsedTimer.exceededMaxTime()) break;
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
