package wss;

/*
Class: Terrain
Description: This class represents the type of terrain in a square. It can be used to determine the movement cost for a character or the types of items that can be found in the square.
             The terrain class is the parent class for the subclasses Plains, Mountain, Desert, Swamp, and Forest.
Variables:
    - int movementCost: the cost of moving through the terrain
    - int waterCost: the cost of water while moving through the terrain
    - int foodCost: the cost of food while moving through the terrain
Methods:
    - getCosts(): returns the movement cost, water cost, and food cost of the terrain
*/

public class Terrain 
{
    private int movementCost;
    private int waterCost;
    private int foodCost;

    public Terrain(int movementCost, int waterCost, int foodCost)
    {
        this.movementCost = movementCost;
        this.waterCost = waterCost;
        this.foodCost = foodCost;
    }

    public int[] getCosts()
    {
        return new int[] {movementCost, waterCost, foodCost};
    }
    
}
