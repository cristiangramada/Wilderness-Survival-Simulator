import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import wss.gui.GameConfig;
import wss.gui.GameWindow;
import wss.gui.NewGameDialog;

/**
 * Prompts for expedition settings, then opens the simulation window.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch (Exception ignored) {
            }
            GameConfig cfg = NewGameDialog.prompt(null);
            if (cfg == null) {
                return;
            }
            new GameWindow(cfg).setVisible(true);
        });
    }
}
