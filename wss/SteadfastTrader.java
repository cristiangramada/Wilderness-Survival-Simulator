package wss;

/**
 * State: generous counters up to two softer asks, then shuts the ledger. Accepts when the explorer
 * largely matches the standing terms (balanced, not infinite giveaways).
 */
public class SteadfastTrader extends Trader {

    @Override
    public TradeOffer openingProposal(Player player) {
        if (player.getWater() < 42) {
            return new TradeOffer(11, 0, 0, 0, 20, 0, "");
        }
        return new TradeOffer(9, 0, 0, 0, 0, 16, "");
    }

    @Override
    public NegotiationOutcome respond(Player player, TradeOffer explorerBid, int round, TradeOffer onTable) {
        if (explorerBid.matchesStanding(onTable)) {
            return NegotiationOutcome.accept();
        }
        if (explorerBid.playerCanPay(player) && generousEnough(explorerBid, onTable)) {
            return NegotiationOutcome.accept();
        }
        if (round >= 3) {
            return NegotiationOutcome.reject();
        }
        if (round < 2) {
            return NegotiationOutcome.counter(onTable.softenAsk(2, 2));
        }
        return NegotiationOutcome.reject();
    }

    private static boolean generousEnough(TradeOffer bid, TradeOffer table) {
        if (!bid.conformsToStallRules() || !table.conformsToStallRules()) {
            return false;
        }
        if (table.getReceiveWater() > 0) {
            return bid.getReceiveFood() == 0 && bid.getReceiveWater() > 0
                    && bid.getGiveGold() >= table.getGiveGold() - 3
                    && bid.getReceiveWater() <= table.getReceiveWater() + 4;
        }
        return bid.getReceiveWater() == 0 && bid.getReceiveFood() > 0
                && bid.getGiveGold() >= table.getGiveGold() - 3
                && bid.getReceiveFood() <= table.getReceiveFood() + 4;
    }
}
