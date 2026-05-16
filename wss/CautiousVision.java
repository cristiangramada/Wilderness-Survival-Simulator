package wss;

// CautiousVision: sees one tile north, one south, and one east
public class CautiousVision extends Vision {

    @Override
    protected int[][] relativeVisibleOffsets() {
        return OFFSETS_CAUTIOUS;
    }
}
