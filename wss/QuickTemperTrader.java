package wss;

/**
 * Trader AI that gets impatient. Only offers one counter before rejecting further attempts.
 */
public class QuickTemperTrader extends Trader {

    @Override
    public TradeOffer openingProposal(Player player) {
        return new TradeOffer(13, 0, 0, 0, 18, 0, "");
    }

    @Override
    public NegotiationOutcome respond(Player player, TradeOffer explorerBid, int round, TradeOffer onTable) {
        if (explorerBid.matchesStanding(onTable)) {
            return NegotiationOutcome.accept();
        }
        if (round >= 2) {
            return NegotiationOutcome.reject();
        }
        if (explorerBid.playerCanPay(player) && tightMatch(explorerBid, onTable)) {
            return NegotiationOutcome.accept();
        }
        if (round == 0) {
            return NegotiationOutcome.counter(onTable.softenAsk(1, 1));
        }
        return NegotiationOutcome.reject();
    }

    private static boolean tightMatch(TradeOffer bid, TradeOffer table) {
        if (!bid.conformsToStallRules()) {
            return false;
        }
        if (Math.abs(bid.getGiveGold() - table.getGiveGold()) > 1) {
            return false;
        }
        if (table.getReceiveWater() > 0) {
            return bid.getReceiveFood() == 0 && bid.getReceiveWater() <= table.getReceiveWater() + 1;
        }
        return bid.getReceiveWater() == 0 && bid.getReceiveFood() <= table.getReceiveFood() + 1;
    }
}
