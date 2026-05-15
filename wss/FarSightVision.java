package wss;

/**
 * Slide scope: extended wedge (two-step verticals and eastern arc).
 */
public class FarSightVision extends Vision {

    @Override
    protected int[][] relativeVisibleOffsets() {
        return OFFSETS_FAR;
    }
}
