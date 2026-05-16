package wss;

// Result of one round of negotiation between the player and a trader (accept, reject, or counter)

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
