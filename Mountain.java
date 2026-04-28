public class Mountain extends Terrain
{
    // Random number -- used during map generation to determine the type of terrain for a square
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
