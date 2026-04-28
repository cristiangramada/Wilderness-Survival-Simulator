public class Desert extends Terrain
{
    // Random number -- used during map generation to determine the type of terrain for a square
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
