package wss.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

import wss.Item;
import wss.Square;
import wss.Terrain;

/**
 * One map cell painting terrain tint, loot icons (food / water / gold), a trader stall figure, and the explorer.
 */
final class TerrainTile extends JPanel {

    private Terrain terrain = null;
    private boolean explorerHere;
    private boolean stallHere;
    private int lootFood;
    private int lootWater;
    private int lootGold;

    TerrainTile(int cellPx) {
        setOpaque(false);
        setPreferredSize(new Dimension(cellPx, cellPx));
    }

    void syncFrom(Square square, boolean playerOnTile) {
        this.terrain = square.getTerrainType();
        this.explorerHere = playerOnTile;
        this.stallHere = square.hasTrader();
        int f = 0;
        int w = 0;
        int g = 0;
        for (Item it : square.getItems()) {
            switch (it.getKind()) {
                case FOOD:
                    f++;
                    break;
                case WATER_SKIN:
                    w++;
                    break;
                case GOLD_CLUMP:
                    g++;
                    break;
            }
        }
        this.lootFood = f;
        this.lootWater = w;
        this.lootGold = g;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics gx) {
        super.paintComponent(gx);
        Graphics2D g2 = (Graphics2D) gx.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        Color fill = TerrainColors.fillFor(terrain);
        g2.setColor(fill);
        g2.fillRect(0, 0, w, h);

        if (stallHere) {
            paintTraderStall(g2, w, h);
        }

        int bx = 5;
        int by = h - 6;
        if (lootFood > 0) {
            paintFoodIcon(g2, bx, by);
            bx += 13;
        }
        if (lootWater > 0) {
            paintWaterIcon(g2, bx, by);
            bx += 13;
        }
        if (lootGold > 0) {
            paintGoldIcon(g2, bx, by);
        }

        if (explorerHere) {
            paintExplorerSilhouette(g2, w, h);
        }

        g2.dispose();
    }

    /**
     * Simple two-tone rover: hood, cloak, backpack — reads at small sizes without external art files.
     */
    private static void paintExplorerSilhouette(Graphics2D g2, int w, int h) {
        float cx = w / 2f;
        float baseY = h * 0.62f;

        g2.setColor(new Color(0, 0, 0, 70));
        g2.fill(new Ellipse2D.Float(cx - 9, baseY + 6, 18, 5));

        g2.setColor(new Color(40, 50, 90));
        g2.fill(new RoundRectangle2D.Float(cx - 8f, baseY - 14f, 16f, 22f, 8f, 8f));

        g2.setColor(new Color(85, 60, 40));
        g2.fillRoundRect((int) (cx - 8), (int) (baseY - 8), 5, 10, 2, 2);

        g2.setColor(new Color(220, 180, 130));
        g2.fill(new Ellipse2D.Float(cx - 5f, baseY - 16f, 10f, 10f));

        g2.setColor(new Color(15, 20, 40));
        RoundRectangle2D hood = new RoundRectangle2D.Float(cx - 6f, baseY - 18f, 12f, 8f, 6f, 6f);
        g2.fill(hood);
    }

    /** Striped awning, counter, and merchant bust — reads as a stall without text. */
    private static void paintTraderStall(Graphics2D g2, int w, int h) {
        float cx = w / 2f;
        int awTop = 2;
        int awBot = 11;

        Path2D awning = new Path2D.Float();
        awning.moveTo(cx - 15, awBot);
        awning.lineTo(cx - 17, awTop);
        awning.lineTo(cx + 17, awTop);
        awning.lineTo(cx + 15, awBot);
        awning.closePath();
        g2.setColor(new Color(110, 35, 40));
        g2.fill(awning);
        g2.setStroke(new BasicStroke(1.2f));
        g2.setColor(new Color(160, 90, 95));
        for (int i = -2; i <= 2; i++) {
            float x = cx + i * 5.5f;
            g2.drawLine((int) x, awTop + 1, (int) (x - 1.5f), awBot - 1);
        }

        g2.setColor(new Color(92, 58, 28));
        g2.fillRoundRect((int) (cx - 13), awBot - 1, 26, 9, 4, 4);
        g2.setColor(new Color(130, 85, 45));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect((int) (cx - 13), awBot - 1, 26, 9, 4, 4);

        g2.setColor(new Color(230, 200, 175));
        g2.fill(new Ellipse2D.Float(cx - 4.5f, awBot - 3f, 9f, 9f));
        g2.setColor(new Color(75, 55, 40));
        g2.fill(new Ellipse2D.Float(cx - 5f, awBot - 5f, 10f, 5f));
        g2.setColor(new Color(55, 45, 70));
        g2.fill(new RoundRectangle2D.Float(cx - 5f, awBot + 1f, 10f, 7f, 3f, 3f));
    }

    /** Small apple / ration at foot of tile. */
    private static void paintFoodIcon(Graphics2D g2, int cx, int baseY) {
        g2.setColor(new Color(35, 80, 35));
        g2.fillOval(cx - 2, baseY - 10, 4, 3);
        g2.setColor(new Color(95, 55, 30));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(cx, baseY - 8, cx, baseY - 11);
        g2.setColor(new Color(210, 55, 45));
        g2.fill(new Ellipse2D.Float(cx - 5f, baseY - 8f, 10f, 9f));
        g2.setColor(new Color(255, 120, 100, 140));
        g2.fill(new Ellipse2D.Float(cx - 3f, baseY - 7f, 4f, 3f));
    }

    /** Water droplet / skin. */
    private static void paintWaterIcon(Graphics2D g2, int cx, int baseY) {
        Path2D drop = new Path2D.Float();
        drop.moveTo(cx, baseY - 11);
        drop.curveTo(cx + 6f, baseY - 6f, cx + 5f, baseY + 1f, cx, baseY + 2);
        drop.curveTo(cx - 5f, baseY + 1f, cx - 6f, baseY - 6f, cx, baseY - 11);
        drop.closePath();
        g2.setColor(new Color(70, 150, 230));
        g2.fill(drop);
        g2.setColor(new Color(180, 230, 255, 200));
        g2.fill(new Ellipse2D.Float(cx - 2f, baseY - 8f, 3f, 3f));
    }

    /** Tiny gold nugget stack when ore lies on the tile. */
    private static void paintGoldIcon(Graphics2D g2, int cx, int baseY) {
        g2.setColor(new Color(210, 165, 40));
        g2.fillOval(cx - 5, baseY - 6, 7, 5);
        g2.setColor(new Color(255, 220, 90));
        g2.fillOval(cx - 2, baseY - 8, 6, 5);
        g2.setColor(new Color(160, 110, 25));
        g2.setStroke(new BasicStroke(0.8f));
        g2.drawOval(cx - 2, baseY - 8, 6, 5);
    }
}
