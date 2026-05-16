package wss;

// FocusedVision: sees directly east, northeast, and southeast
public class FocusedVision extends Vision {

    @Override
    protected int[][] relativeVisibleOffsets() {
        return OFFSETS_FOCUSED;
    }
}
