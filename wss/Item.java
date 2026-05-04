package wss;

import java.util.Random;

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
