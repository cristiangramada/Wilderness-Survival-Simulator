package wss;

/**
 * Represents a trader on a tile. The player and trader exchange TradeOffer objects
 * back and forth until one side accepts or gives up.
 */
public abstract class Trader {

    /** Same label for every subclass - the player cannot tell which type they are dealing with. */
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
