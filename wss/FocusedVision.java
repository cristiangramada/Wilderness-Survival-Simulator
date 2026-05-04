package wss;

/**
 * Slide scope: (1,0), (1,1), (1,-1) relative to the player.
 */
public class FocusedVision extends Vision {

    @Override
    protected int[][] relativeVisibleOffsets() {
        return OFFSETS_FOCUSED;
    }
}
