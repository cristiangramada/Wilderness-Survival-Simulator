package wss.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import wss.Item;
import wss.Map;
import wss.Player;
import wss.Square;
import wss.Terrain;

/**
 * Colored terrain grid with painted loot/trader/player accents.
 */
final class MapPanel extends JPanel {

    private static final int CELL = 42;

    private final GameController controller;
    private final TerrainTile[][] cells;
    private final int width;
    private final int height;
    private final Runnable afterStep;
    private final boolean interactiveMoves;

    MapPanel(GameController controller, Runnable afterStep, boolean interactiveMoves) {
        this.controller = controller;
        this.afterStep = afterStep;
        this.interactiveMoves = interactiveMoves;
        Map map = controller.getMap();
        this.width = map.getWidth();
        this.height = map.getHeight();
        setLayout(new GridLayout(height, width, 0, 0));
        setBorder(new EmptyBorder(6, 6, 6, 6));
        setBackground(new Color(28, 28, 32));

        cells = new TerrainTile[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TerrainTile tile = new TerrainTile(CELL);
                if (interactiveMoves) {
                    int cx = x;
                    int cy = y;
                    tile.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            onCellClicked(cx, cy);
                        }
                    });
                }
                cells[y][x] = tile;
                add(tile);
            }
        }
        setPreferredSize(new Dimension(width * CELL, height * CELL));
        refreshCells();
    }

    void refreshCells() {
        Map map = controller.getMap();
        Player player = controller.getPlayer();
        int[] p = player.getCurrentSquare().getCoordinates();
        int px = p[0];
        int py = p[1];

        Color idleEdge = new Color(22, 22, 26);
        Color heroEdge = new Color(255, 255, 230);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Square sq = map.getSquare(x, y);
                TerrainTile tile = cells[y][x];
                boolean explorerHere = x == px && y == py;
                tile.syncFrom(sq, explorerHere);
                tile.setBorder(BorderFactory.createLineBorder(explorerHere ? heroEdge : idleEdge,
                        explorerHere ? 3 : 1));

                Terrain terrain = sq.getTerrainType();
                String note;
                if (sq.hasTrader()) {
                    note = explorerHere ? "Trader Stall; you stand here." : "Trader Stall";
                } else {
                    String loot = summarizeLoot(sq);
                    note = explorerHere ? loot + "; you stand here." : loot;
                }
                String tip = String.format("<html>%s @ [%d,%d]<br/>%s</html>",
                        terrainLabel(terrain),
                        x,
                        y,
                        note);
                tile.setToolTipText(tip);
            }
        }
        repaint();
    }

    private static String summarizeLoot(Square sq) {
        if (sq.getItems().isEmpty()) {
            return "No loose loot visible";
        }
        StringBuilder b = new StringBuilder("Loot spotted: ");
        int n = 0;
        for (Item it : sq.getItems()) {
            if (n++ > 0) {
                b.append(", ");
            }
            b.append(it.getName());
        }
        return b.toString();
    }

    void stepFromArrow(int dx, int dy) {
        if (!interactiveMoves || !controller.movesAllowed()) {
            return;
        }
        if (controller.tryStep(dx, dy)) {
            afterStep.run();
            refreshCells();
        }
    }

    private void onCellClicked(int x, int y) {
        if (!interactiveMoves || !controller.movesAllowed()) {
            return;
        }
        Player player = controller.getPlayer();
        int[] p = player.getCurrentSquare().getCoordinates();
        int dx = x - p[0];
        int dy = y - p[1];
        if (Math.abs(dx) > 1 || Math.abs(dy) > 1 || (dx == 0 && dy == 0)) {
            return;
        }
        if (controller.tryStep(dx, dy)) {
            afterStep.run();
            refreshCells();
        }
    }

    private static String terrainLabel(Terrain t) {
        return t == null ? "Unset" : t.getClass().getSimpleName();
    }
}
