package wss;

/**
 * A person on a square: negotiation is an alternating sequence of {@link TradeOffer}s until someone
 * accepts or walks away. Subclasses differ in temperament (the explorer cannot tell which logic runs).
 */
public abstract class Trader {

    /** Same face for every subclass — only behavior differs behind the curtain. */
    public final String stallLabel() {
        return "Merchant at the stall";
    }

    /** Merchant's first standing terms (what you must give / what they will return). */
    public abstract TradeOffer openingProposal(Player player);

    /**
     * React to the explorer's counter-offer. {@code onTable} is the merchant's current standing ask
     * (opening or their last counter). {@code round} counts explorer proposals after the opening (0 first).
     */
    public abstract NegotiationOutcome respond(Player player, TradeOffer explorerBid,
            int round, TradeOffer onTable);
}
