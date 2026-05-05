package wss.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import wss.Brain;
import wss.Item;
import wss.Map;
import wss.Move;
import wss.PerceivedTile;
import wss.Player;
import wss.Square;
import wss.Trader;
import wss.Vision;

/**
 * Connects Swing views to the game model: timer-driven moves, pickups, stalls, victory.
 */
public class GameController {

    private final Map map;
    private final Player player;
    private boolean allowMoves = true;

    /** Fired once on EDT when player steps onto the rightmost column ({@code width - 1}). */
    private final Runnable onVictory;

    /** Fired when food or water hits zero after a step (after loot/traders resolve). */
    private final Runnable onStarvation;

    private final Brain tradeBrain;
    /** When set, trader visits append one journal line via {@link TraderAutopilot}. */
    private final Consumer<String> traderJournal;

    public GameController(Map map, Player player, Runnable onVictory,
            Runnable onStarvation, Brain tradeBrain, Consumer<String> traderJournal) {
        this.map = map;
        this.player = player;
        this.onVictory = onVictory;
        this.onStarvation = onStarvation;
        this.tradeBrain = tradeBrain;
        this.traderJournal = traderJournal;
    }

    public Map getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean movesAllowed() {
        return allowMoves;
    }

    /** Invoked on a timer: vision surveys tiles, brain picks a bearing, then {@link #tryStep}. */
    public void stepAutopilot() {
        if (!allowMoves) {
            return;
        }
        Vision vision = player.getVision();
        Brain brain = player.getBrain();
        if (vision == null || brain == null) {
            return;
        }
        List<PerceivedTile> options = vision.survey(player, map);
        if (options.isEmpty()) {
            return;
        }
        int[] delta = brain.makeMove(player, map);
        if (delta == null) {
            PerceivedTile fallback = options.get(0);
            delta = new int[] { fallback.dx, fallback.dy };
        }
        if (delta[0] != 0 || delta[1] != 0) {
            tryStep(delta[0], delta[1]);
        }
    }

    public boolean tryStep(int deltaX, int deltaY) {
        if (!allowMoves) {
            return false;
        }
        if (deltaX == 0 && deltaY == 0) {
            return false;
        }
        if (Math.abs(deltaX) > 1 || Math.abs(deltaY) > 1) {
            return false;
        }

        Move move = new Move(map, player, deltaX, deltaY);
        if (!move.isValid()) {
            return false;
        }

        move.execute();
        player.recordStep(deltaX, deltaY);
        resolveAfterLanding();

        Square now = player.getCurrentSquare();
        int x = now.getCoordinates()[0];
        if (x == map.getWidth() - 1) {
            allowMoves = false;
            if (onVictory != null) {
                SwingUtilities.invokeLater(onVictory);
            }
        }
        else if (player.getFood() <= 0 || player.getWater() <= 0) {
            allowMoves = false;
            if (onStarvation != null) {
                SwingUtilities.invokeLater(onStarvation);
            }
        }

        return true;
    }

    private void resolveAfterLanding() {
        Square sq = player.getCurrentSquare();

        List<Item> pile = new ArrayList<>(sq.getItems());
        for (Item it : pile) {
            player.pickup(it);
            sq.removeItem(it);
        }

        Trader merchant = sq.getTrader();
        if (merchant != null && tradeBrain != null) {
            String line = TraderAutopilot.resolve(player, merchant, tradeBrain);
            if (traderJournal != null && line != null && !line.isBlank()) {
                traderJournal.accept(line);
            }
        }
    }

    public void resetAllowMoves(boolean allow) {
        this.allowMoves = allow;
    }
}
