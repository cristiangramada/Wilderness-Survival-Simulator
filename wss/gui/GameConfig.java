package wss.gui;

import wss.BrainKind;
import wss.VisionKind;

/**
 * Expedition settings gathered before the simulation window opens.
 */
public record GameConfig(
        int width,
        int height,
        String difficulty,
        BrainKind brainKind,
        VisionKind visionKind,
        int stepDelayMs
) {
}
