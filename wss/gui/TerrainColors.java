package wss.gui;

import java.awt.Color;

import wss.Desert;
import wss.Forest;
import wss.Mountain;
import wss.Plains;
import wss.Swamp;
import wss.Terrain;

final class TerrainColors {

    private TerrainColors() {
    }

    static Color fillFor(Terrain terrain) {
        if (terrain instanceof Plains) {
            return new Color(186, 222, 145);
        }
        if (terrain instanceof Forest) {
            return new Color(34, 120, 45);
        }
        if (terrain instanceof Desert) {
            return new Color(235, 205, 120);
        }
        if (terrain instanceof Mountain) {
            return new Color(130, 130, 135);
        }
        if (terrain instanceof Swamp) {
            return new Color(105, 90, 60);
        }
        return new Color(200, 200, 200);
    }
}
