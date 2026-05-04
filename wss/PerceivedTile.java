package wss;

/**
 * One legal single-step option from the player's position, decorated by a {@link Vision} implementation.
 */
public final class PerceivedTile {
    public final int dx;
    public final int dy;
    public final Square destination;
    public final int movementCost;
    public final int waterCost;
    public final int foodCost;
    public final int totalCost;
    /** True when the terrain looks harsh for cautious planners. */
    public final boolean risky;
    /**
     * Keen-Eyed: rough penalty if we kept marching the same bearing for a second step (two hops outlook).
     */
    public final int doubleStepOutlook;
    /**
     * Far-Sight: mean movement cost along the open corridor straight east from the explorer (lower is easier marching).
     */
    public final int eastCorridorMovementAvg;

    public PerceivedTile(int dx, int dy, Square destination,
            int movementCost, int waterCost, int foodCost,
            boolean risky, int doubleStepOutlook, int eastCorridorMovementAvg) {
        this.dx = dx;
        this.dy = dy;
        this.destination = destination;
        this.movementCost = movementCost;
        this.waterCost = waterCost;
        this.foodCost = foodCost;
        this.totalCost = movementCost + waterCost + foodCost;
        this.risky = risky;
        this.doubleStepOutlook = doubleStepOutlook;
        this.eastCorridorMovementAvg = eastCorridorMovementAvg;
    }

    public PerceivedTile withRisk(boolean risky) {
        return new PerceivedTile(dx, dy, destination, movementCost, waterCost, foodCost,
                risky, doubleStepOutlook, eastCorridorMovementAvg);
    }

    public PerceivedTile withOutlook(int doubleStep, int eastAvg) {
        return new PerceivedTile(dx, dy, destination, movementCost, waterCost, foodCost,
                risky, doubleStep, eastAvg);
    }
}
