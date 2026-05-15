package wss;

/**
 * Class: Desert
 * Parent Class: Terrain
 * 
 * Description: This class represents the desert terrain type in the simulation. It has specific movement, water, and food costs associated with the terrain's characterisitics.
 * Variables:
 *     - random_num: An integer used during map generation. It is set to 4, which corresponds to the desert terrain type in the map generation algorithm.
 * 
 */
public class Desert extends Terrain
{
    private int random_num = 4;
    public Desert()
    {
        /**
         * Movement cost: 5 -- Sandy terrain, requires more effort to move through than plains but less than mountains
         * Water cost: 6 -- Water sources are scarce, its important to preserve water
         * Food cost: 5  -- Food resources minimal but can be found in the form of small animals and plants
         */
        super(5, 6, 5);
    }

}
