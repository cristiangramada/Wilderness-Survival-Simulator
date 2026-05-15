package wss.gui;

import wss.Brain;
import wss.GreedyEastBrain;
import wss.NegotiationOutcome;
import wss.Player;
import wss.SurvivalBrain;
import wss.TradeOffer;
import wss.Trader;

/**
 * Simulates a short negotiation for the trail journal runs.
 */
final class TraderAutopilot {

    private TraderAutopilot() {
    }

    static String resolve(Player player, Trader trader, Brain brain) {
        TradeOffer onTable = trader.openingProposal(player);
        StringBuilder log = new StringBuilder();
        log.append(trader.stallLabel()).append(" — opening: ").append(onTable.plainStallLine());

        if (maybeInstantAccept(player, brain, onTable, log)) {
            return log.toString();
        }

        for (int round = 0; round < 5; round++) {
            TradeOffer bid = proposeAutopilot(player, brain, onTable, round);
            if (bid == null) {
                log.append(" | You step back without bidding.");
                return log.toString();
            }

            NegotiationOutcome out = trader.respond(player, bid, round, onTable);
            if (out.getKind() == NegotiationOutcome.Kind.ACCEPT) {
                if (!bid.playerCanPay(player)) {
                    log.append(" | Merchant nods, but you cannot pay what you bid — deal falls through.");
                    return log.toString();
                }
                bid.apply(player);
                log.append(" | AGREED: ").append(bid.plainStallLine());
                return log.toString();
            }
            if (out.getKind() == NegotiationOutcome.Kind.REJECT) {
                log.append(" | Merchant refuses your terms.");
                return log.toString();
            }
            onTable = out.getCounterOffer();
            if (onTable == null) {
                log.append(" | Stall closes without a counter.");
                return log.toString();
            }
            log.append(" | Counter: ").append(onTable.plainStallLine());
        }
        log.append(" | Too many rounds — you move on.");
        return log.toString();
    }

    private static boolean maybeInstantAccept(Player player, Brain brain, TradeOffer onTable, StringBuilder log) {
        if (!onTable.playerCanPay(player)) {
            return false;
        }
        if (brain instanceof SurvivalBrain) {
            boolean wantWater = onTable.getReceiveWater() >= 8 && player.getWater() < 40;
            boolean wantFood = onTable.getReceiveFood() >= 6 && player.getFood() < 38;
            if (wantWater || wantFood) {
                onTable.apply(player);
                log.append(" | You accept their opening on the spot.");
                return true;
            }
        }
        if (brain instanceof GreedyEastBrain) {
            if (onTable.getGiveGold() <= 11 && onTable.ledgerWeight() <= 25) {
                onTable.apply(player);
                log.append(" | You snap up their opening — cheap enough.");
                return true;
            }
        }
        return false;
    }

    private static TradeOffer proposeAutopilot(Player player, Brain brain, TradeOffer onTable, int round) {
        if (brain instanceof SurvivalBrain) {
            if (round > 0 && onTable.getGiveGold() > 0) {
                return onTable.copy();
            }
            int g = Math.max(1, onTable.getGiveGold() - 2);
            if (onTable.getReceiveWater() > 0) {
                return new TradeOffer(g, 0, 0, 0, onTable.getReceiveWater(), 0, "");
            }
            if (onTable.getReceiveFood() > 0) {
                return new TradeOffer(g, 0, 0, 0, 0, onTable.getReceiveFood(), "");
            }
            return onTable.copy();
        }
        if (brain instanceof GreedyEastBrain) {
            int offerGold = Math.max(6, onTable.getGiveGold() - 3 - round);
            int rw = onTable.getReceiveWater() > 0 ? Math.max(1, onTable.getReceiveWater() - 2) : 0;
            int rf = onTable.getReceiveFood() > 0 ? Math.max(1, onTable.getReceiveFood() - 1) : 0;
            return new TradeOffer(offerGold, 0, 0, 0, rw, rf, "");
        }
        return onTable.copy();
    }
}
