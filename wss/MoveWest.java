package wss;

public class MoveWest extends Move{
    
    public MoveWest(Map map, Player player) {
        /**
         * new x-coordinate relative to Player's current position: -1 (moves left 1)
         * new y-coordinate relative to Player's current position: 0 (stays the same)
         **/
        super(map, player, -1, 0);
    }
}