package wss;

import java.util.List;

/**
 * Slide scope: each subclass lists cells visible relative to the player; {@link #survey} limits legal
 * one-step choices to those offsets. Procedures return a {@link Path} (costed sequence of king-steps)
 * toward the best matching visible resource or stall.
 */
public abstract class Vision {

    /** Focused: east-forward cone (slide example at origin). */
    public static final int[][] OFFSETS_FOCUSED = { { 1, 0 }, { 1, 1 }, { 1, -1 } };
    /** Cautious: north, south, east only. */
    public static final int[][] OFFSETS_CAUTIOUS = { { 0, 1 }, { 0, -1 }, { 1, 0 } };
    /** Keen-Eyed: wider forward arc including two east. */
    public static final int[][] OFFSETS_KEEN = {
            { 0, 1 }, { 1, 1 }, { 1, 0 }, { 2, 0 }, { 0, -1 }, { 1, -1 }
    };
    /** Far-Sight: extended wedge including two-step verticals and eastern flank. */
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
