package wss;

import java.util.List;

/**
 * Decides the next single step using {@link Vision} procedures and/or the scoped neighbor survey.
 */
public abstract class Brain {

    /**
     * Slide-style entry: consult vision paths, then fall back to terrain scoring on {@link Vision#survey}.
     */
    public int[] makeMove(Player player, Map map) {
        return pickStep(player, player.getVision().survey(player, map));
    }

    /**
     * @return relative step {@code (dx,dy)} or {@code null} to stand still (no valid choice).
     */
    public abstract int[] pickStep(Player player, List<PerceivedTile> options);
}
