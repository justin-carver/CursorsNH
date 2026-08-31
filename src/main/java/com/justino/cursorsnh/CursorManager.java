package com.justino.cursorsnh;

import com.justino.cursorsnh.CursorRegistry.Entry;
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

    /** Check if a cursor pack is loaded, and does it have a default cursor */
    public static void applyDefault() {
        Entry entry = CursorRegistry.get("default");
        if (entry == null) {
            reset();
        } else {
            setCursor(entry.image, entry.xHot, entry.yHot);
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
