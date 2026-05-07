package wss;
/**
 * Class: Plains
 * Parent Class: Terrain
 * Description: This class represents the plains terrain type in the simulation. It has specific movement, water, and food costs associated with the terrain's characterisitics.
 * Variables:
 *    - random_num: An integer used during map generation. It is set to 1, which corresponds to the plains terrain type in the map generation algorithm.
 */

public class Plains extends Terrain
{
    private int random_num = 1;

    public Plains()
    {
        /**
         * Movement cost: 3 -- Mostly flat land, requires little to no effort to move through
         * Water cost: 4 -- Water sources can be scarce, its important to preserve water
         * Food cost: 3  -- Food resources minimal but can be found in the form of small animals and plants
         */
        super(1, 3, 3);
    }
    
}
