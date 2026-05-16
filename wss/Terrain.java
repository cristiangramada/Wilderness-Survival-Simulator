package wss;

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
