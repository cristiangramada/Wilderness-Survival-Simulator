package wss;

// FarSightVision: widest range, includes two steps north/south and a broad eastern arc
public class FarSightVision extends Vision {

    @Override
    protected int[][] relativeVisibleOffsets() {
        return OFFSETS_FAR;
    }
}
