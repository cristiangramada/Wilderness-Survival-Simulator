package wss;

/*
Class: Move
Description: This class represents moves that can be taken by the Player.
             It connects the Player, Brain, Vision, and Map classes.
Variables:
    - Map map: the Map in the game
    - Player player: the Player in the game
    - int currentX: the Player's current x-coordinate
    - int currentY: the Player's current y-coordinate
    - int newX: the new x-coordinate of the new Square relative to the current coordinate
    - int newY: the new y-coordinate of the new Square relative to the current coordinate
Methods:
    - getNewX(): returns new x-coordinate
    - getNewY(): returns new y-coordinate
    - execute():
*/

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

    /** Target absolute coordinates (after applying delta). Only meaningful if {@link #isValid()}. */
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
