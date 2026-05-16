package wss;

import java.util.ArrayList;

public class Square
{
    private int x;
    private int y;
    private Terrain terrain_type;
    private ArrayList<Item> items = new ArrayList<>();
    private Trader trader;

    public Square(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.terrain_type = new Plains(); // default until map generation sets it
    }

    public int[] getCoordinates()
    {
        return new int[] { x, y };
    }

    public void generateBonus()
    {
        // not implemented yet
    }

    public ArrayList<Item> getItems() 
    {
        return items;
    }

    public Trader getTrader()
    {
        return trader;
    }

    public void setTrader(Trader trader)
    {
        this.trader = trader;
    }

    public boolean hasTrader()
    {
        return trader != null;
    }

    public Terrain getTerrainType() 
    {
        return terrain_type;
    }

    public Terrain setTerrain(String terrain)
    {
        switch (terrain)
        {
            case "Plains":
                terrain_type = new Plains();
                break;
            case "Forest":
                terrain_type = new Forest();
                break;
            case "Mountain":
                terrain_type = new Mountain();
                break;
            case "Swamp":
                terrain_type = new Swamp();
                break;
            case "Desert":
                terrain_type = new Desert();
                break;
            default:
                break;
        }
        
        return terrain_type;
    }

    public void addItem(Item item)
    {
        items.add(item);

    }
    public void removeItem(Item item)
    {
        items.remove(item);
    }

}