package wss;
public class Swamp extends Terrain 
{
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
