package wss;

public class MoveSouthWest extends Move{
    
    public MoveSouthWest(Map map, Player player) {
        /**
         * new x-coordinate relative to Player's current position: -1 (moves right 1)
         * new y-coordinate relative to Player's current position: 1 (moves down 1)
         **/
        super(map, player, -1, 1);
    }
}