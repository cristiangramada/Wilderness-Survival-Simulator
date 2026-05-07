package wss;
/**
 * Class: Forest
 * Parent Class: Terrain
 * 
 * Description: This class represents the forest terrain type in the simulation. It has specific movement, water, and food costs associated with the terrain's characterisitics.
 * Variables:
 *     - random_num: An integer used during map generation. It is set to 2, which  corresponds to the forest terrain type in the map generation algorithm.
 * 
 */
public class Forest extends Terrain
{
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
