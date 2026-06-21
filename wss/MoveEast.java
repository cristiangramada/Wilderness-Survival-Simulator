package wss;

public class MoveEast extends Move{
    
    public MoveEast(Map map, Player player) {
        /**
         * new x-coordinate relative to Player's current position: 1 (moves right 1)
         * new y-coordinate relative to Player's current position: 0 (stays the same)
         **/
        super(map, player, 1, 0);
    }
}