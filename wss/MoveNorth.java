package wss;

public class MoveNorth extends Move{
    
    public MoveNorth(Map map, Player player) {
        /**
         * new x-coordinate relative to Player's current position: 0 (stays the same)
         * new y-coordinate relative to Player's current positiont: -1 (moves up 1)
         **/
        super(map, player, 0, -1);
    }
}
