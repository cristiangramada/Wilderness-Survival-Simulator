package wss;

/**
 * Configures which {@link Vision} implementation surveys neighboring tiles.
 */
public enum VisionKind {
    FOCUSED,
    CAUTIOUS,
    KEEN_EYED,
    FAR_SIGHT;

    public Vision create() {
        switch (this) {
            case FOCUSED:
                return new FocusedVision();
            case CAUTIOUS:
                return new CautiousVision();
            case KEEN_EYED:
                return new KeenEyedVision();
            case FAR_SIGHT:
                return new FarSightVision();
            default:
                throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case FOCUSED:
                return "Focused";
            case CAUTIOUS:
                return "Cautious";
            case KEEN_EYED:
                return "Keen-Eyed";
            case FAR_SIGHT:
                return "Far-Sight";
            default:
                return name();
        }
    }
}
