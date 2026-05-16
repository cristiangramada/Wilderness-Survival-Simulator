package wss;
import java.util.List;

/*
 * Class: Brain
 * Description: Abstract class for player decision-making.
 *              makeMove() surveys visible tiles using the player's vision,
 *              then calls pickStep() to choose where to move next.
 *
 * Methods:
 *     - makeMove(Player player, Map map): gets visible tiles and calls pickStep.
 *     - pickStep(Player player, List<PerceivedTile> options): picks the actual step.
 *              Returns (dx, dy) or null to stay put.
 *
 */

public abstract class Brain {

    /**
     * Calls vision.survey() to get the list of visible options, then pickStep() to choose.
     */
    public int[] makeMove(Player player, Map map) {
        return pickStep(player, player.getVision().survey(player, map));
    }

    // returns the (dx, dy) step to take, or null if there's nowhere to go
    public abstract int[] pickStep(Player player, List<PerceivedTile> options);
}
