package wss;

public class MoveNorthEast extends Move{
    
    public MoveNorthEast(Map map, Player player) {
        /**
         * new x-coordinate relative to Player's current position: 1 (moves left 1)
         * new y-coordinate relative to Player's current positiont: -1 (moves up 1)
         **/
        super(map, player, 1, -1);
    }
}