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

// Handles game logic on each timer tick: moving the player, picking up loot, trading, and end conditions.
public class GameController {

    private final Map map;
    private final Player player;
    private boolean allowMoves = true;

    private final Runnable onVictory; // called when player reaches the east edge
    private final Runnable onStarvation; // called when food or water hits 0

    private final Brain tradeBrain;
    private final Consumer<String> traderJournal; // appends trade results to the journal text area

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
