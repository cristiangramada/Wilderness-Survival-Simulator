package wss;

/**
 * Configures which {@link Brain} implementation the automaton uses.
 */
public enum BrainKind {
    SURVIVAL,
    GREEDY_EAST;

    public Brain create() {
        switch (this) {
            case SURVIVAL:
                return new SurvivalBrain();
            case GREEDY_EAST:
                return new GreedyEastBrain();
            default:
                throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case SURVIVAL:
                return "SurvivalBrain";
            case GREEDY_EAST:
                return "GreedyEastBrain";
            default:
                return name();
        }
    }
}
