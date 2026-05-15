package wss;
import java.util.List;

/**
 * Class: Brain
 * Description: This abstract class represents the decision-making component of a player in the simulation. 
 *              It provides a method to determine the next move based on the player's vision and the current position on the map.
 * Methods:
 *     - makeMove(Player player, Map map): This method uses the player's vision to survey the surroundings and then calls the pickStep method to determine the next move.
 *     - pickStep(Player player, List<PerceivedTile> options): This abstract method is implemented by subclasses to decide the next step based on the perceived tiles. It returns a relative step (dx, dy) or null to stand still if there are no valid choices.
 */

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
