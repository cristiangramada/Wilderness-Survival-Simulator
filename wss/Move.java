package wss;

public class Move {

    private Map map;
    private Player player;
    private int currentX;
    private int currentY;
    private int newX;
    private int newY;

    public Move(Map map, Player player, int newX, int newY) {
        this.map = map;
        this.player = player;
        this.currentX = player.getCurrentSquare().getCoordinates()[0];
        this.currentY = player.getCurrentSquare().getCoordinates()[1];
        this.newX = newX;
        this.newY = newY;
    }

    public int getNewX() {
        return newX;
    }

    public int getNewY() {
        return newY;
    }

    public boolean isValid() {
        int destX = currentX + newX;
        int destY = currentY + newY;
        return map.isInBounds(destX, destY);
    }

    public int[] getDestination() {
        return new int[] { currentX + newX, currentY + newY };
    }

    public void execute() {
        if (!isValid()) {
            return;
        }
        Square newSquare = map.getSquare(currentX + newX, currentY + newY);
        Terrain terrain = newSquare.getTerrainType();
        if (terrain == null) {
            return;
        }
        int[] costs = terrain.getCosts();
        player.applyTerrainCosts(costs[0], costs[1], costs[2]);
        player.moveTo(newSquare);
    }
}
