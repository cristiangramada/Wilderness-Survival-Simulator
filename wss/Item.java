package wss;
import java.util.Random;
/**
 * Class: Item
 * Description: This class represents an item that can be found in the world and collected by the player. Each item has a name, a kind (food, water skin, or gold clump), and a value that represents its effect when collected.
 * Variables:
 *     - name: A string representing the name of the item.
 *     - kind: An enum representing the type of item (food, water skin, or gold clump).
 *     - value: An integer representing the effect magnitude of the item (e.g., how much food or water it provides, or how much gold it is worth).
 * 
 * Methods:
 *     - getName(): Returns the name of the item.
 *     - getKind(): Returns the kind of the item.
 *     - getValue(): Returns the value of the item.
 *     - randomSurfaceFind(Random rnd): A static method that generates a random item based on a random number generator. The method randomly selects an item type and assigns it a value within a certain range based on the type.
 *  
 */

/**
 * World pickup: categorized loot with a numeric strength used when the player collects it.
 */
public class Item {

    public enum Kind {
        FOOD,
        WATER_SKIN,
        GOLD_CLUMP
    }

    private final String name;
    private final Kind kind;
    /** Effect magnitude (food/water healed, or gold amount). */
    private final int value;

    public Item(String name, Kind kind, int value) {
        this.name = name;
        this.kind = kind;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Kind getKind() {
        return kind;
    }

    public int getValue() {
        return value;
    }

    /**
     * Random surface loot biased toward snacks and skins of water; occasional ore.
     */
    public static Item randomSurfaceFind(Random rnd) {
        Kind k = Kind.values()[rnd.nextInt(Kind.values().length)];
        switch (k) {
            case FOOD:
                return new Item("Food", Kind.FOOD, 6 + rnd.nextInt(14));
            case WATER_SKIN:
                return new Item("Water", Kind.WATER_SKIN, 8 + rnd.nextInt(16));
            case GOLD_CLUMP:
            default:
                return new Item("Gold", Kind.GOLD_CLUMP, 4 + rnd.nextInt(11));
        }
    }
}
