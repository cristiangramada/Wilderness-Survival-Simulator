package wss.gui;

import java.awt.BorderLayout;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.UIManager;

import wss.Brain;
import wss.Map;
import wss.Player;
import wss.Vision;

/**
 * Simulation shell: map grid, stats sidebar, timed brain-driven stepping.
 */
public class GameWindow extends JFrame {

    private final Random randomStart = new Random();
    private Timer expeditionTimer;

    public GameWindow(GameConfig cfg) {
        super("WSS expedition");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ignored) {
        }

        setTitle(String.format("WSS — %s · %s · %s",
                cfg.brainKind().toString(),
                cfg.visionKind().toString(),
                cfg.difficulty()));

        Map map = new Map(cfg.width(), cfg.height(), cfg.difficulty());
        int startY = randomStart.nextInt(map.getHeight());
        Player player = new Player(map, 0, startY, 100, 100, 100, 40);

        Brain brain = cfg.brainKind().create();
        Vision vision = cfg.visionKind().create();
        player.attachBrain(brain);
        player.attachVision(vision);

        map.populateWorldFeatures(player);

        JTextArea trailJournal = new JTextArea(4, 72);
        trailJournal.setEditable(false);
        trailJournal.setLineWrap(true);
        trailJournal.setWrapStyleWord(true);
        trailJournal.setText("Trail journal — trader stops and haggles appear below.\n");

        Runnable onVictory = () -> {
            if (expeditionTimer != null) {
                expeditionTimer.stop();
            }
            JOptionPane.showMessageDialog(GameWindow.this,
                    "You reached the eastern frontier. Cross-complete!",
                    "Goal reached",
                    JOptionPane.INFORMATION_MESSAGE);
        };

        Runnable onStarvation = () -> {
            if (expeditionTimer != null) {
                expeditionTimer.stop();
            }
            JOptionPane.showMessageDialog(GameWindow.this,
                    "Food or water has run out — the expedition ends here.",
                    "Game over",
                    JOptionPane.WARNING_MESSAGE);
        };

        GameController controller =
                new GameController(map, player, GameWindow.this, onVictory, onStarvation, true, brain,
                        line -> {
                            trailJournal.append(line + "\n");
                            trailJournal.setCaretPosition(trailJournal.getDocument().getLength());
                        });
        StatsPanel stats = new StatsPanel(StatsPanel.demoLegendTerrain());
        Runnable refreshHud = () -> stats.applyPlayer(controller.getPlayer());
        MapPanel mapPanel = new MapPanel(controller, refreshHud, false);

        JPanel center = new JPanel(new BorderLayout(10, 0));
        center.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        center.add(mapPanel, BorderLayout.CENTER);
        center.add(stats, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(center, BorderLayout.CENTER);

        JScrollPane journalScroll = new JScrollPane(trailJournal);
        journalScroll.setBorder(BorderFactory.createTitledBorder("Trail journal"));
        add(journalScroll, BorderLayout.SOUTH);

        stats.applyPlayer(player);

        expeditionTimer = new Timer(cfg.stepDelayMs(), e -> {
            if (!controller.movesAllowed()) {
                expeditionTimer.stop();
                return;
            }
            controller.stepAutopilot();
            mapPanel.refreshCells();
            stats.applyPlayer(controller.getPlayer());
        });
        expeditionTimer.setInitialDelay(600);
        expeditionTimer.start();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }
}
