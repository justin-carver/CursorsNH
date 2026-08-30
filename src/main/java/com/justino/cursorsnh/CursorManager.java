package com.justino.cursorsnh;

import net.minecraftforge.common.config.Configuration;

import java.awt.image.BufferedImage;

public class CursorManager {
    private static BufferedImage currentImage;
    private static int currentXHot, currentYHot;

    /** Hotspot is in top-left origin (pack convention). */
    public static void setCursor(BufferedImage image, int xHot, int yHot) {
        currentImage = image;
        currentXHot = xHot;
        currentYHot = yHot;

        if (Config.cursorNative) {
            VirtualCursorRenderer.hide();
            CursorNative.setCursorFromImage(image, xHot, yHot);
        } else {
            CursorNative.reset();
            VirtualCursorRenderer.show(image, xHot, yHot);
        }
    }

    public static void reset() {
        currentImage = null;
        CursorNative.reset();
        VirtualCursorRenderer.hide();
    }
}
