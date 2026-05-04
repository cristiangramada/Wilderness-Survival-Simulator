package wss;

/*
Class: Map
Description: This class represents the map in the WSS. It is made of squares from the Square class, and holds
             objects from the Item and Player classes.
             The player can set the difficutly and size of the map.
Variables:
    - int width: the width of the map
    - int height: the height of the map
    - String difficulty: how difficult the game is
    - Square[][] squares: the matrix of Square objects that makes up the map
Methods:
    - getSquare(int x, int y): returns the Square at coordinate (x,y)
    - getDifficulty(): returns the game difficulty
    - getSize(): returns the size of the map as an array
    - generateMap(): creates a Square matrix using the width and height
*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Map {

    private final int width;
    private final int height;
    private final String difficulty;
    private Square[][] squares;

    public Map(int width, int height, String difficulty) {
        this.width = width;
        this.height = height;
        this.difficulty = difficulty;
        this.squares = new Square[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                squares[y][x] = new Square(x, y);
            }
        }
        generateMap();
    }

    /**
     * Sprinkles pickups and installs a couple of traders (mixed personalities, not too many) once spawn is known.
     * Keeps loot off the frontier columns so the trek reads clearly.
     */
    public void populateWorldFeatures(Player player) {
        if (player == null || player.getCurrentSquare() == null) {
            return;
        }
        int[] spawn = player.getCurrentSquare().getCoordinates();
        int sx = spawn[0];
        int sy = spawn[1];

        Random rnd = new Random();
        double itemChance = 0.11;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == sx && y == sy) {
                    continue;
                }
                if (x == 0 || x == width - 1) {
                    continue;
                }
                if (!squares[y][x].hasTrader() && rnd.nextDouble() < itemChance) {
                    squares[y][x].addItem(Item.randomSurfaceFind(rnd));
                }
            }
        }

        List<int[]> stalls = candidateStallCells(sx, sy);
        Collections.shuffle(stalls, rnd);
        Trader[] personalities = {
                new SteadfastTrader(), new QuickTemperTrader(), new WheelerTrader()
        };
        Collections.shuffle(java.util.Arrays.asList(personalities), rnd);
        if (stalls.size() >= 2) {
            squares[stalls.get(0)[1]][stalls.get(0)[0]].setTrader(personalities[0]);
            squares[stalls.get(1)[1]][stalls.get(1)[0]].setTrader(personalities[1]);
        }
        else if (stalls.size() == 1) {
            squares[stalls.get(0)[1]][stalls.get(0)[0]].setTrader(
                    personalities[rnd.nextInt(personalities.length)]);
        }
    }

    /**
     * Interior tiles only, skips spawn and goal rims, skips tiles that already spawned loot for cleaner stalls.
     */
    private List<int[]> candidateStallCells(int spawnX, int spawnY) {
        List<int[]> coords = new ArrayList<>();
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 2; x++) {
                if (x == spawnX && y == spawnY) {
                    continue;
                }
                if (!squares[y][x].getItems().isEmpty()) {
                    continue;
                }
                coords.add(new int[] { x, y });
            }
        }
        return coords;
    }

    /** Kept for older designs that constructed the backing array externally. */
    public Map(int width, int height, String difficulty, Square[][] squares) {
        this.width = width;
        this.height = height;
        this.difficulty = difficulty;
        this.squares = squares;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Square getSquare(int x, int y) {
        return squares[y][x];
    }

    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int[] getSize() {
        return new int[] { width, height };
    }

    public void generateMap() {
        Random rand = new Random();
        String[] pool;
        if (difficulty.equalsIgnoreCase("easy")) {
            pool = new String[] { "Plains", "Forest", "Swamp" };
        }
        else if (difficulty.equalsIgnoreCase("medium")) {
            pool = new String[] { "Plains", "Forest", "Swamp", "Desert" };
        }
        else {
            pool = new String[] { "Plains", "Forest", "Swamp", "Desert", "Mountain" };
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                String terrainName = pool[rand.nextInt(pool.length)];
                squares[i][j].setTerrain(terrainName);
            }
        }
    }
}
