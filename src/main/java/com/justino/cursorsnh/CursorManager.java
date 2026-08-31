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
            VirtualCursorRenderer.show(image, xHot, yHot);
            CursorNative.hide();
        }
    }

    public static void reapply() {
        if (currentImage != null) {
            setCursor(currentImage, currentXHot, currentYHot);
        }
    }

    public static void reset() {
        currentImage = null;
        CursorNative.reset();
        VirtualCursorRenderer.hide();
    }
}
