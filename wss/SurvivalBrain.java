package wss;

import java.util.List;

/**
 * Cautious brain - prioritizes finding food and water when running low before moving east.
 */
public class SurvivalBrain extends Brain {

    @Override
    public int[] makeMove(Player player, Map map) {
        Vision v = player.getVision();
        if (player.getWater() < 38) {
            int[] d = firstLegalStep(v.closestWater(player, map), player, map);
            if (d != null) {
                return d;
            }
        }
        if (player.getFood() < 38) {
            int[] d = firstLegalStep(v.closestFood(player, map), player, map);
            if (d != null) {
                return d;
            }
        }
        return pickStep(player, v.survey(player, map));
    }

    private static int[] firstLegalStep(Path path, Player player, Map map) {
        if (path == null || !path.hasStep()) {
            return null;
        }
        int[] d = path.peekFirstDelta();
        if (d == null) {
            return null;
        }
        Move m = new Move(map, player, d[0], d[1]);
        return m.isValid() ? d : null;
    }

    @Override
    public int[] pickStep(Player player, List<PerceivedTile> options) {
        if (options.isEmpty()) {
            return null;
        }
        int w = player.getWater();
        int f = player.getFood();
        boolean thirsty = w < 40;
        boolean hungry = f < 40;

        int lastDx = player.getLastStepDx();
        int lastDy = player.getLastStepDy();

        PerceivedTile pick = null;
        int best = Integer.MIN_VALUE;
        for (PerceivedTile t : options) {
            int score = 0;
            score -= t.totalCost * 4;
            if (thirsty) {
                score -= t.waterCost * 6;
            }
            if (hungry) {
                score -= t.foodCost * 6;
            }
            if (t.risky) {
                score -= 45;
            }
            score -= t.doubleStepOutlook;
            score -= t.eastCorridorMovementAvg * 3;
            score += t.dx * 55;
            if ((lastDx != 0 || lastDy != 0) && t.dx == -lastDx && t.dy == -lastDy) {
                score -= 160;
            }
            if (score > best) {
                best = score;
                pick = t;
            }
        }
        return pick == null ? null : new int[] { pick.dx, pick.dy };
    }
}
