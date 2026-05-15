package wss;
/**
 * Class: Mountain
 * Parent Class: Terrain
 * 
 * Description: This class represents the mountain terrain type in the simulation. It has specific movement, water, and food costs associated with the terrain's characterisitics.
 * Variables:
 *   - random_num: An integer used during map generation. It is set to 5, which corresponds to the mountain terrain type in the map generation algorithm.
 * 
 */

public class Mountain extends Terrain
{
    private int random_num = 5;

    public Mountain()
    {
        /**
         * Movement cost: 8 -- Steep and rocky terrain, requires a lot of effort to move through
         * Water cost: 4 -- Water sources can be scarce, but can be found in mountain streams or snow
         * Food cost: 6  -- Food resources scarce, must preserve food obtained from previous squares
         */
        
        super(8, 4, 6);
    }

}
