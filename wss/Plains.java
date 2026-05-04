package wss;


public class Plains extends Terrain
{
    // Random number -- used during map generation to determine the type of terrain for a square
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
