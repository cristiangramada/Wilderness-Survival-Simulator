package wss;
/**
 * Class: Swamp
 * Parent Class: Terrain
 * Description: This class represents the swamp terrain type in the simulation. It has specific movement, water, and food costs associated with the terrain's characterisitics.
 * Variables:
 *     - random_num: An integer used during map generation. It is set to 3
 * 
 */
public class Swamp extends Terrain 
{
    // Random number -- used during map generation to determine the type of terrain for a square
    private int random_num = 3;
    public Swamp()
    {
        /**
         * Movement cost: 4 -- Terrain is wet and muddy, making it difficult to travel on foot
         * Water cost: 4 -- Water sources found may be contaminated, so it may require extra effort to purify.
         * Food cost: 3 -- Food sources can be found in the form of fish, amphibians, etc, but may require effort to catch.
         */
        super(4, 4, 3);
    }

}
