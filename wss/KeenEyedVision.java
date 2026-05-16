package wss;

// KeenEyedVision: sees 6 tiles including two steps east on the same row
public class KeenEyedVision extends Vision {

    @Override
    protected int[][] relativeVisibleOffsets() {
        return OFFSETS_KEEN;
    }
}
