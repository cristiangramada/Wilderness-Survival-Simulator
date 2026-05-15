package wss;

/**
 * Slide scope: (0,1), (0,-1), (1,0) — one north, one south, one east.
 */
public class CautiousVision extends Vision {

    @Override
    protected int[][] relativeVisibleOffsets() {
        return OFFSETS_CAUTIOUS;
    }
}
