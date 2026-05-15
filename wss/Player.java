package wss;

/**
 * Tracks the player's position on the map and survival resources.
 */
public class Player {
    private final Map map;
    private Square currentSquare;
    private Brain brain;
    private Vision vision;
    /** Last move deltas (for brains that avoid ping-ponging). */
    private int lastStepDx;
    private int lastStepDy;
    private int health;
    private int water;
    private int food;
    private int gold;

    private static final int FOOD_WATER_CAP = 100;
    private static final int HEALTH_CAP = 250;

    public Player(Map map, int startX, int startY, int startingHealth,
                  int startingWater, int startingFood, int startingGold) {
        this.map = map;
        this.currentSquare = map.getSquare(startX, startY);
        this.health = startingHealth;
        this.water = startingWater;
        this.food = startingFood;
        this.gold = startingGold;
    }

    public Map getMap() {
        return map;
    }

    public Square getCurrentSquare() {
        return currentSquare;
    }

    public void moveTo(Square square) {
        this.currentSquare = square;
    }

    public void attachBrain(Brain brain) {
        this.brain = brain;
    }

    public void attachVision(Vision vision) {
        this.vision = vision;
    }

    public Brain getBrain() {
        return brain;
    }

    public Vision getVision() {
        return vision;
    }

    /** Records the relative step just taken (orthogonal or diagonal). */
    public void recordStep(int dx, int dy) {
        this.lastStepDx = dx;
        this.lastStepDy = dy;
    }

    public int getLastStepDx() {
        return lastStepDx;
    }

    public int getLastStepDy() {
        return lastStepDy;
    }

    public int getHealth() {
        return health;
    }

    public int getWater() {
        return water;
    }

    public int getFood() {
        return food;
    }

    public int getGold() {
        return gold;
    }

    /**
     * Apply costs from terrain when entering a square: movement, water, food (in that order per {@link Terrain#getCosts()}).
     */
    public void applyTerrainCosts(int movementCost, int waterCost, int foodCost) {
        health = clampHealth(health - movementCost);
        water = clampFoodWater(water - waterCost);
        food = clampFoodWater(food - foodCost);
    }

    public void addGold(int amount) {
        gold = Math.max(0, gold + amount);
    }

    public void addWater(int amount) {
        if (amount == 0) {
            return;
        }
        water = clampFoodWater(water + amount);
    }

    public void addFood(int amount) {
        if (amount == 0) {
            return;
        }
        food = clampFoodWater(food + amount);
    }

    public void heal(int points) {
        if (points == 0) {
            return;
        }
        health = clampHealth(health + points);
    }

    /** Apply a floor pickup rolled during map generation or trading. */
    public void pickup(Item item) {
        switch (item.getKind()) {
            case FOOD:
                addFood(item.getValue());
                break;
            case WATER_SKIN:
                addWater(item.getValue());
                break;
            case GOLD_CLUMP:
                addGold(item.getValue());
                break;
            default:
                break;
        }
    }

    private static int clampFoodWater(int v) {
        return Math.min(FOOD_WATER_CAP, Math.max(0, v));
    }

    private static int clampHealth(int v) {
        return Math.min(HEALTH_CAP, Math.max(0, v));
    }
}
