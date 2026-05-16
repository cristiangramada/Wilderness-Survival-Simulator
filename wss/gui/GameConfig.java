package wss.gui;

import wss.BrainKind;
import wss.VisionKind;

// Holds the settings chosen in the new game dialog
public record GameConfig(
        int width,
        int height,
        String difficulty,
        BrainKind brainKind,
        VisionKind visionKind,
        int stepDelayMs
) {
}
