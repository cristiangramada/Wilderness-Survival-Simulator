package wss;

/**
 * Trader's reply to an explorer-proposed {@link TradeOffer}.
 */
public final class NegotiationOutcome {

    public enum Kind {
        ACCEPT,
        REJECT,
        COUNTER
    }

    private final Kind kind;
    private final TradeOffer counterOffer;

    private NegotiationOutcome(Kind kind, TradeOffer counterOffer) {
        this.kind = kind;
        this.counterOffer = counterOffer;
    }

    public static NegotiationOutcome accept() {
        return new NegotiationOutcome(Kind.ACCEPT, null);
    }

    public static NegotiationOutcome reject() {
        return new NegotiationOutcome(Kind.REJECT, null);
    }

    public static NegotiationOutcome counter(TradeOffer nextTerms) {
        return new NegotiationOutcome(Kind.COUNTER, nextTerms);
    }

    public Kind getKind() {
        return kind;
    }

    public TradeOffer getCounterOffer() {
        return counterOffer;
    }
}
