package wss.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import wss.Player;
import wss.Terrain;

// Right-side panel showing health/water/food/gold and the terrain color legend
final class StatsPanel extends JPanel {

    private final JLabel healthValue = styledValue();
    private final JLabel waterValue = styledValue();
    private final JLabel foodValue = styledValue();
    private final JLabel goldValue = styledValue();

    StatsPanel(Map<String, Terrain> legendTerrainsByLabel) {
        super(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(12, 12, 12, 12));
        setPreferredSize(new Dimension(240, 0));

        JPanel core = new JPanel(new GridLayout(0, 1, 0, 8));
        core.setOpaque(false);

        core.add(metricRow("Health", healthValue));
        core.add(metricRow("Water", waterValue));
        core.add(metricRow("Food", foodValue));
        core.add(metricRow("Gold", goldValue));

        JPanel wrapped = new JPanel(new BorderLayout());
        wrapped.setBorder(BorderFactory.createTitledBorder("Explorer status"));
        wrapped.add(core, BorderLayout.NORTH);

        JPanel legendPanel = legend(legendTerrainsByLabel);

        add(wrapped, BorderLayout.NORTH);
        add(legendPanel, BorderLayout.CENTER);
    }

    void applyPlayer(Player player) {
        healthValue.setText(Integer.toString(player.getHealth()));
        waterValue.setText(Integer.toString(player.getWater()));
        foodValue.setText(Integer.toString(player.getFood()));
        goldValue.setText(Integer.toString(player.getGold()));
    }

    private static JPanel metricRow(String title, JLabel valueLabel) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        JLabel caption = new JLabel(title);
        caption.setFont(caption.getFont().deriveFont(Font.PLAIN, 13f));
        row.add(caption, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);
        return row;
    }

    private static JLabel styledValue() {
        JLabel lbl = new JLabel("--", SwingConstants.RIGHT);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 16f));
        return lbl;
    }

    private static JPanel legend(java.util.Map<String, Terrain> terrainsByKey) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Terrain map key"));
        for (Map.Entry<String, Terrain> e : terrainsByKey.entrySet()) {
            JPanel row = new JPanel(new BorderLayout(6, 0));
            JLabel swatch = new JLabel("  ");
            swatch.setOpaque(true);
            Terrain t = e.getValue();
            swatch.setBackground(TerrainColors.fillFor(t));
            swatch.setBorder(BorderFactory.createLineBorder(new java.awt.Color(40, 40, 40)));
            JLabel name = new JLabel(e.getKey());
            name.setFont(name.getFont().deriveFont(Font.PLAIN, 11f));
            row.add(swatch, BorderLayout.WEST);
            row.add(name, BorderLayout.CENTER);
            panel.add(row);
        }
        return panel;
    }

    static Map<String, Terrain> demoLegendTerrain() {
        Map<String, Terrain> map = new LinkedHashMap<>();
        map.put("Plains", new wss.Plains());
        map.put("Forest", new wss.Forest());
        map.put("Swamp", new wss.Swamp());
        map.put("Desert", new wss.Desert());
        map.put("Mountain", new wss.Mountain());
        return map;
    }
}
