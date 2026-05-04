package wss.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import wss.NegotiationOutcome;
import wss.Player;
import wss.TradeOffer;
import wss.Trader;

/**
 * Alternating offers until accept, reject, or walk away — matches slide negotiation loop.
 */
final class TraderDialog {

    private TraderDialog() {
    }

    static void encounter(Window owner, Player player, Trader trader) {
        TradeOffer onTable = trader.openingProposal(player);
        int explorerRound = 0;

        while (true) {
            String tableHtml = offerHtml(onTable);
            String[] opts = { "Accept their terms", "Propose your trade", "Walk away" };
            int pick = JOptionPane.showOptionDialog(owner,
                    tableHtml,
                    trader.stallLabel(),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opts,
                    opts[0]);

            if (pick == JOptionPane.CLOSED_OPTION || pick == 2) {
                return;
            }

            if (pick == 0) {
                if (!onTable.playerCanPay(player)) {
                    JOptionPane.showMessageDialog(owner,
                            "You cannot cover those costs right now.",
                            trader.stallLabel(),
                            JOptionPane.WARNING_MESSAGE);
                    continue;
                }
                onTable.apply(player);
                JOptionPane.showMessageDialog(owner,
                        "Trade complete.",
                        "Settled",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            TradeOffer bid = readExplorerProposal(owner, trader);
            if (bid == null) {
                continue;
            }

            NegotiationOutcome out = trader.respond(player, bid, explorerRound, onTable);
            explorerRound++;

            if (out.getKind() == NegotiationOutcome.Kind.ACCEPT) {
                if (!bid.playerCanPay(player)) {
                    JOptionPane.showMessageDialog(owner,
                            "You cannot pay what you just proposed.",
                            trader.stallLabel(),
                            JOptionPane.WARNING_MESSAGE);
                    continue;
                }
                bid.apply(player);
                JOptionPane.showMessageDialog(owner,
                        "Deal.",
                        "Settled",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (out.getKind() == NegotiationOutcome.Kind.REJECT) {
                JOptionPane.showMessageDialog(owner,
                        "No deal.",
                        trader.stallLabel(),
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            onTable = out.getCounterOffer();
            JOptionPane.showMessageDialog(owner,
                    offerHtml(onTable),
                    trader.stallLabel(),
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    private static TradeOffer readExplorerProposal(Window owner, Trader trader) {
        JPanel grid = new JPanel(new GridLayout(0, 2, 6, 4));
        JSpinner gGive = new JSpinner(new SpinnerNumberModel(1, 0, 500, 1));
        JRadioButton wantWater = new JRadioButton("Water", true);
        JRadioButton wantFood = new JRadioButton("Food", false);
        ButtonGroup wantGroup = new ButtonGroup();
        wantGroup.add(wantWater);
        wantGroup.add(wantFood);
        JSpinner amountRecv = new JSpinner(new SpinnerNumberModel(8, 1, 500, 1));

        grid.add(new JLabel("You pay (gold)"));
        grid.add(gGive);
        grid.add(new JLabel("You want"));
        JPanel wantRow = new JPanel(new GridLayout(1, 2, 4, 0));
        wantRow.add(wantWater);
        wantRow.add(wantFood);
        grid.add(wantRow);
        grid.add(new JLabel("Amount"));
        grid.add(amountRecv);

        JPanel wrap = new JPanel(new BorderLayout(0, 8));
        wrap.add(new JLabel("<html><body style=\"width:320px\">Same rules as their stall: you pay <b>gold</b> only, "
                + "and you ask for either <b>water</b> <i>or</i> <b>food</b> in one offer — not both.</body></html>"),
                BorderLayout.NORTH);
        wrap.add(grid, BorderLayout.CENTER);
        wrap.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        int ok = JOptionPane.showConfirmDialog(owner, wrap, trader.stallLabel() + " — your offer",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (ok != JOptionPane.OK_OPTION) {
            return null;
        }

        int gold = ((Number) gGive.getValue()).intValue();
        int amt = ((Number) amountRecv.getValue()).intValue();
        int rw = wantWater.isSelected() ? amt : 0;
        int rf = wantFood.isSelected() ? amt : 0;
        TradeOffer bid = new TradeOffer(gold, 0, 0, 0, rw, rf, "");
        if (!bid.conformsToStallRules() || gold < 1) {
            JOptionPane.showMessageDialog(owner,
                    "Pick gold to pay (at least 1) and exactly one kind — water or food — with a positive amount.",
                    trader.stallLabel(),
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return bid;
    }

    private static String offerHtml(TradeOffer o) {
        return "<html><body style=\"width:280px\"><p style=\"font-size:14pt;margin:8px 0\">"
                + escape(o.plainStallLine()) + "</p></body></html>";
    }

    private static String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
