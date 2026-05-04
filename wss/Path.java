package wss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sequence of steps with accumulated terrain costs (movement, water, food).
 * Supports both {@link Move}-based construction and planned delta chains from vision pathfinding.
 */
public class Path {

    private final Map map;
    private ArrayList<Move> moves = new ArrayList<>();
    private int totalMovementCost;
    private int totalWaterCost;
    private int totalFoodCost;

    /** When set, {@link #peekFirstDelta()} reads the first compass step without using {@link Move} objects. */
    private List<int[]> plannedDeltas;

    public Path(Map map, ArrayList<Move> moves) {
        this.map = map;
        this.moves = moves != null ? moves : new ArrayList<>();
        this.totalMovementCost = 0;
        this.totalWaterCost = 0;
        this.totalFoodCost = 0;
    }

    public static Path empty(Map map) {
        Path p = new Path(map, new ArrayList<>());
        p.plannedDeltas = new ArrayList<>();
        return p;
    }

    /**
     * Path with precomputed costs and relative steps (each step one king-move).
     */
    public static Path planned(Map map, List<int[]> deltas, int movement, int water, int food) {
        Path p = new Path(map, new ArrayList<>());
        p.plannedDeltas = new ArrayList<>(deltas);
        p.totalMovementCost = movement;
        p.totalWaterCost = water;
        p.totalFoodCost = food;
        return p;
    }

    public void addMove(Move m) {
        moves.add(m);
        if (!m.isValid()) {
            return;
        }
        int[] dest = m.getDestination();
        Terrain terrain = map.getSquare(dest[0], dest[1]).getTerrainType();
        int[] costs = terrain.getCosts();
        totalMovementCost += costs[0];
        totalWaterCost += costs[1];
        totalFoodCost += costs[2];
    }

    /** First relative step of a planned path, or derived from the first {@link Move} if present. */
    public int[] peekFirstDelta() {
        if (plannedDeltas != null && !plannedDeltas.isEmpty()) {
            return new int[] { plannedDeltas.get(0)[0], plannedDeltas.get(0)[1] };
        }
        if (!moves.isEmpty()) {
            Move m0 = moves.get(0);
            return new int[] { m0.getNewX(), m0.getNewY() };
        }
        return null;
    }

    public boolean hasStep() {
        return peekFirstDelta() != null;
    }

    public String returnSummary() {
        return "Path Cost Summary:\n"
                + "------------------\n"
                + "Total Movement Cost: " + totalMovementCost
                + "\nTotal Water Cost: " + totalWaterCost
                + "\nTotal Food Cost: " + totalFoodCost;
    }
}
