package wss;

/*
Class: Square
Description: This class represent a square space in a map. It connects the program to Item and Terrain classes. 
Variables:
    - int x: coordinate of the square in the x-axis
    - int y: coordinate of the square in the y-axis
    - Terrain terrain_type: the type of terrain in the square
    - ArrayList<Item> items: optional items on this square (empty until bonus/item logic is enabled)

    Methods:
    - Suare(int x, int y, Terrain terrain_type): constructor for the Square class
    - getItems(): returns the items in the square
    - getTerrainType(): returns the terrain type of the square
    - setTerrain(String terrain): sets the terrain type of the square based on the string input
    - addItem(Item item): adds an item to the square
    - removeItem(Item item): removes an item from the square
    - generateBonus(): generates bonus items in the square based on the bonus probability

TODO: 
    1. Assign values to the items in the generateBonus() method
    2. Determine bonus item limits per square?
    3. Add the Item object onto the generateBonus method
*/

import java.util.ArrayList;

public class Square
{
    private int x;
    private int y;
    private Terrain terrain_type;
    private ArrayList<Item> items = new ArrayList<>();
    /** Optional wandering merchant occupying this stall tile. */
    private Trader trader;

    public Square(int x, int y)
    {
        this.x = x;
        this.y = y;
        /* Default terrain until Map generation assigns one (keeps getters safe for callers). */
        this.terrain_type = new Plains();
        /* Bonus items intentionally disabled until item/trader gameplay is modeled. */
    }

    /**
     * @return { x, y } in map coordinates matching {@link Map#getSquare(int, int)}.
     */
    public int[] getCoordinates()
    {
        return new int[] { x, y };
    }

    public void generateBonus()
    {
        /* Reserved for teams that later wire items / traders onto the map. */
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