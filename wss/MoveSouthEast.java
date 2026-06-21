package wss;

public class MoveSouthEast extends Move{
    
    public MoveSouthEast(Map map, Player player) {
        /**
         * new x-coordinate relative to Player's current position: 1 (moves left 1)
         * new y-coordinate relative to Player's current position: 1 (moves down 1)
         **/
        super(map, player, 1, 1);
    }
}