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
    - getSquare(x: int, y: int): returns the Square at coordinate (x,y)
    - getDifficulty(): returns the game difficulty
    - setDifficulty(difficulty: String): sets the difficulty of the game
    - getSize(): returns the size of the map as an array
    - setSize(width: int, height: int): sets the size of the map
    - generateMap(): creates a Square matrix using the width and height
*/

public class Map {

    private int width;
    private int height;
    private String difficulty;
    private Square[][] squares;

    public Square getSquare(int x, int y) {
        return squares[y][x];
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int[] getSize() {
        return new int[] {width, height};
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void generateMap() {
        this.squares = new Square[this.height][this.width];

        //TODO: terrain generation with difficulty
    }
}