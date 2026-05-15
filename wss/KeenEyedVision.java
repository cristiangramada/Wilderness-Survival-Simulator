package wss;

/**
 * Slide scope: six cells including two steps east on the same row.
 */
public class KeenEyedVision extends Vision {

    @Override
    protected int[][] relativeVisibleOffsets() {
        return OFFSETS_KEEN;
    }
}
