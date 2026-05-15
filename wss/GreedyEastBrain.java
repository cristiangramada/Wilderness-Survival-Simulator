package wss;

import java.util.List;

/**
 * Pushes east; prefers {@link Vision#easiestPath} through visible terrain, then scored survey steps.
 */
public class GreedyEastBrain extends Brain {

    @Override
    public int[] makeMove(Player player, Map map) {
        Vision v = player.getVision();
        Path easy = v.easiestPath(player, map);
        if (easy != null && easy.hasStep()) {
            int[] d = easy.peekFirstDelta();
            if (d != null) {
                Move m = new Move(map, player, d[0], d[1]);
                if (m.isValid()) {
                    return d;
                }
            }
        }
        return pickStep(player, v.survey(player, map));
    }

    @Override
    public int[] pickStep(Player player, List<PerceivedTile> options) {
        if (options.isEmpty()) {
            return null;
        }
        int lastDx = player.getLastStepDx();
        int lastDy = player.getLastStepDy();

        PerceivedTile pick = null;
        int best = Integer.MIN_VALUE;
        for (PerceivedTile t : options) {
            int score = t.dx * 800;
            score -= t.totalCost * 2;
            score -= t.eastCorridorMovementAvg * 3;
            if (t.risky) {
                score -= 180;
            }
            score -= t.doubleStepOutlook * 2;
            if (t.dx > 0) {
                score += 120;
            }
            if ((lastDx != 0 || lastDy != 0) && t.dx == -lastDx && t.dy == -lastDy) {
                score -= 200;
            }
            if (score > best) {
                best = score;
                pick = t;
            }
        }
        return pick == null ? null : new int[] { pick.dx, pick.dy };
    }
}
