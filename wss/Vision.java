package wss;

import java.util.List;

/*
 * Abstract class for the different vision types.
 * Each subclass defines which offsets (dx, dy) the player can see.
 * The survey method uses those offsets to build the list of visible tiles.
 */
public abstract class Vision {

    // Focused: sees one east, one northeast, one southeast
    public static final int[][] OFFSETS_FOCUSED = { { 1, 0 }, { 1, 1 }, { 1, -1 } };
    // Cautious: only looks north, south, and east
    public static final int[][] OFFSETS_CAUTIOUS = { { 0, 1 }, { 0, -1 }, { 1, 0 } };
    // Keen-Eyed: wider arc, includes two tiles east on the same row
    public static final int[][] OFFSETS_KEEN = {
            { 0, 1 }, { 1, 1 }, { 1, 0 }, { 2, 0 }, { 0, -1 }, { 1, -1 }
    };
    // Far-Sight: largest footprint, two steps in several directions
    public static final int[][] OFFSETS_FAR = {
            { 0, 1 }, { 0, 2 }, { 1, 2 }, { 1, 1 }, { 1, 0 }, { 0, -1 }, { 0, -2 },
            { 1, -1 }, { 1, -2 }, { 2, 0 }, { 2, 1 }, { 2, -1 }
    };

    protected abstract int[][] relativeVisibleOffsets();

    public List<PerceivedTile> survey(Player player, Map map) {
        return VisionSupport.scopedSurvey(player, map, relativeVisibleOffsets());
    }

    public Path closestFood(Player player, Map map) {
        return VisionSupport.closestOfKind(player, map, relativeVisibleOffsets(), Item.Kind.FOOD);
    }

    public Path closestWater(Player player, Map map) {
        return VisionSupport.closestOfKind(player, map, relativeVisibleOffsets(), Item.Kind.WATER_SKIN);
    }

    public Path closestGold(Player player, Map map) {
        return VisionSupport.closestOfKind(player, map, relativeVisibleOffsets(), Item.Kind.GOLD_CLUMP);
    }

    public Path closestTrader(Player player, Map map) {
        return VisionSupport.closestTrader(player, map, relativeVisibleOffsets());
    }

    public Path secondClosestFood(Player player, Map map) {
        return VisionSupport.secondClosestOfKind(player, map, relativeVisibleOffsets(), Item.Kind.FOOD);
    }

    public Path secondClosestWater(Player player, Map map) {
        return VisionSupport.secondClosestOfKind(player, map, relativeVisibleOffsets(), Item.Kind.WATER_SKIN);
    }

    public Path secondClosestGold(Player player, Map map) {
        return VisionSupport.secondClosestOfKind(player, map, relativeVisibleOffsets(), Item.Kind.GOLD_CLUMP);
    }

    public Path secondClosestTrader(Player player, Map map) {
        return VisionSupport.secondClosestTrader(player, map, relativeVisibleOffsets());
    }

    public Path easiestPath(Player player, Map map) {
        return VisionSupport.easiestVisiblePath(player, map, relativeVisibleOffsets());
    }
}
