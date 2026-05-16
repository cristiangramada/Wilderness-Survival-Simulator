package wss;
public class Plains extends Terrain
{
    public Plains()
    {
        /**
         * Movement cost: 1 -- Flat open land, easiest terrain to cross
         * Water cost: 3 -- Some water sources available but not always reliable
         * Food cost: 3 -- Small animals and plants can be found with some effort
         */
        super(1, 3, 3);
    }
    
}
