/*
Class: Square
Description: This class represent a square space in a map. It connects the program to Item and Terrain classes. 
Variables:
    - int x: coordinate of the square in the x-axis
    - int y: coordinate of the square in the y-axis
    - Terrain terrain_type: the type of terrain in the square
    - Item[] items: the items that are in the square
Methods:
    - getItems(): returns the items in the square
    - getTerrainType(): returns the terrain type of the square
    - addItem(Item item): adds an item to the square
    - removeItem(Item item): removes an item from the square

TODO: 
    -- array list errors remove when Item class is implemented
    -- test the add and remove item methods when Item class is implemented
*/

import java.util.ArrayList;

public class Square
{
    private int x;
    private int y;
    private Terrain terrain_type;
    private ArrayList<Item> items = new ArrayList<>();

    public Square(int x, int y, Terrain terrain_type, ArrayList<Item> items)
    {
        this.x = x;
        this.y = y;
        this.terrain_type = terrain_type;
        this.items = items;
    }

    public ArrayList<Item> getItems() 
    {
        return items;
    }

    public Terrain getTerrainType() 
    {
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