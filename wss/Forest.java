package wss;

public class Forest extends Terrain
{
    // Random number -- used during map generation to determine the type of terrain for a square
    private int random_num = 2;
    public Forest()
    {
        /**
         * Movement cost: 4 -- Dense forest, requires more effort to travel and find paths through the trees
         * Water cost: 2 -- Water sources can be found in the form of streams and ponds, but may require some effort to find
         * Food cost: 2 -- Food resources can be found in abundance in the form of animals and plants.
         */ 
        super(4, 2, 2);

    }

}
