package wss;

// Holds info about one tile the player could step onto this turn.
// Vision subclasses fill in the extra fields (risky, doubleStepOutlook, eastCorridorMovementAvg)
// so the brain can use them when scoring options.
public final class PerceivedTile {
    public final int dx;
    public final int dy;
    public final Square destination;
    public final int movementCost;
    public final int waterCost;
    public final int foodCost;
    public final int totalCost;
    public final boolean risky; // true if CautiousVision considers this tile dangerous
    public final int doubleStepOutlook; // used by KeenEyed to look two steps ahead
    public final int eastCorridorMovementAvg; // used by FarSight to check the eastern path difficulty

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
