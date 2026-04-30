/*
Class: Square
Description: This class represent a square space in a map. It connects the program to Item and Terrain classes. 
Variables:
    - int x: coordinate of the square in the x-axis
    - int y: coordinate of the square in the y-axis
    - int bonus_prob: the probability of a bonus items being in the square
    - Terrain terrain_type: the type of terrain in the square
    - ArrayList<Item> items: array list ofthe items that are in the square
    - Random rand: random number generator used to determine random events in the square, such as bonus items, and the type of bonus item.

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
import java.util.Random;

public class Square
{
    private int x;
    private int y;
    private int bonus_prob;
    private Terrain terrain_type;
    private ArrayList<Item> items = new ArrayList<>();
    private Random rand = new Random();

    public Square(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.bonus_prob = rand.nextInt(100) + 1;
        if (bonus_prob >= 80)
        {
            generateBonus();
        }
    }

    public void generateBonus()
    {
        // TODO: 1, 2, 3
        // *might change num of items generated**
       for(int i = 0; i < 3; i++)
        {
            int item_type = rand.nextInt(3) + 1;
            switch (item_type)
            {
                case 1: 
                    items.add();
                    break;
                case 2: 
                    items.add();
                    break;
                case 3:
                    items.add();
                    break;
                default:
                    break;
            }
            
        }
    }

    public ArrayList<Item> getItems() 
    {
        if(items.isEmpty())
        {
            System.out.println("There are no bonus items in this square.");
        }
        return items;
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