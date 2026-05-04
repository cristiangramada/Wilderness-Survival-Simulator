package wss;

import java.util.Random;

/**
 * State: restless — sometimes snaps closed, sometimes tosses a whimsical counter, mood jittered by a
 * hidden RNG stream so repeat visits feel uneven without being a charity.
 */
public class WheelerTrader extends Trader {

    @Override
    public TradeOffer openingProposal(Player player) {
        int tweak = player.getGold() > 30 ? 2 : 0;
        int price = 10 + tweak;
        if (player.getWater() <= player.getFood()) {
            return new TradeOffer(price, 0, 0, 0, 16, 0, "");
        }
        return new TradeOffer(price, 0, 0, 0, 0, 12, "");
    }

    @Override
    public NegotiationOutcome respond(Player player, TradeOffer explorerBid, int round, TradeOffer onTable) {
        if (explorerBid.matchesStanding(onTable)) {
            return NegotiationOutcome.accept();
        }
        Random rng = new Random((long) onTable.hashCode() * 13L + round * 7919L + player.getGold());
        if (rng.nextDouble() < 0.12) {
            return NegotiationOutcome.reject();
        }
        if (explorerBid.playerCanPay(player) && explorerBid.ledgerWeight() >= onTable.ledgerWeight() - 6) {
            return NegotiationOutcome.accept();
        }
        if (round < 4 && rng.nextDouble() < 0.55) {
            int dg = rng.nextInt(3);
            int dw = rng.nextInt(2);
            return NegotiationOutcome.counter(onTable.softenAsk(dg, dw));
        }
        return round >= 4 ? NegotiationOutcome.reject() : NegotiationOutcome.counter(onTable.softenAsk(1, 0));
    }
}
