package wss.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import wss.BrainKind;
import wss.VisionKind;

// Dialog shown at startup to collect map size, difficulty, brain type, vision type, and step speed
public final class NewGameDialog {

    private NewGameDialog() {
    }

    public static GameConfig prompt(java.awt.Component parent) {
        JPanel form = new JPanel(new BorderLayout(8, 8));
        JPanel grid = new JPanel(new GridLayout(0, 2, 8, 8));

        JSpinner widthSpin = new JSpinner(new SpinnerNumberModel(5, 5, 45, 1));
        JSpinner heightSpin = new JSpinner(new SpinnerNumberModel(5, 5, 35, 1));
        JComboBox<String> diffBox = new JComboBox<>(new String[] { "easy", "medium", "hard" });
        diffBox.setSelectedItem("easy");

        JComboBox<BrainKind> brainBox = new JComboBox<>(BrainKind.values());
        JComboBox<VisionKind> visionBox = new JComboBox<>(VisionKind.values());

        JSpinner delaySpin = new JSpinner(new SpinnerNumberModel(1000, 400, 6000, 250));

        grid.add(new JLabel("Map width"));
        grid.add(widthSpin);
        grid.add(new JLabel("Map height"));
        grid.add(heightSpin);
        grid.add(new JLabel("Difficulty"));
        grid.add(diffBox);
        grid.add(new JLabel("Brain"));
        grid.add(brainBox);
        grid.add(new JLabel("Vision"));
        grid.add(visionBox);
        grid.add(new JLabel("Milliseconds between steps"));
        grid.add(delaySpin);

        JLabel hint = new JLabel("<html><small>Difficulty controls terrain variety (easy fewest biomes, hard includes mountains). "
                + "After OK the explorer runs itself using brain + vision.</small></html>");
        hint.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        form.add(grid, BorderLayout.CENTER);
        form.add(hint, BorderLayout.SOUTH);

        int ok = JOptionPane.showConfirmDialog(parent, form, "Configure expedition",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (ok != JOptionPane.OK_OPTION) {
            return null;
        }

        int w = ((Number) widthSpin.getValue()).intValue();
        int h = ((Number) heightSpin.getValue()).intValue();
        String diff = (String) diffBox.getSelectedItem();
        BrainKind bk = (BrainKind) brainBox.getSelectedItem();
        VisionKind vk = (VisionKind) visionBox.getSelectedItem();
        int delayMs = ((Number) delaySpin.getValue()).intValue();

        return new GameConfig(w, h, diff, bk, vk, delayMs);
    }
}
