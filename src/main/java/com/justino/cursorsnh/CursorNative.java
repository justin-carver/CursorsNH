package com.justino.cursorsnh;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

public class CursorNative {

    private static Cursor current;

    public static void setCursor(int width, int height, int xHot, int yHot, IntBuffer argb) {
        // TODO: Get this dynamically from ResourcePack
        int yHotFromPack = 0; // Debug value for now.
        int yHotShim = height - 1 - yHotFromPack;

        try {
            Cursor next = new Cursor(width, height, xHot, yHotShim, 1, argb, null);
            Mouse.setNativeCursor(next);

            if (current != null) {
                current.destroy();
            }
            current = next;
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    /** A 1x1 fully transparent cursor, to hide the OS pointer. */
    public static void setBlank() {
        try {
            IntBuffer blank = BufferUtils.createIntBuffer(1);
            blank.put(0, 0x00000000);
            Mouse.setNativeCursor(new Cursor(1, 1, 0, 0, 1, blank, null));
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    /** Takes a top-left-origin hotspot and handles the flip internally. */
    public static void setCursorFromImage(BufferedImage image, int xHot, int yHotTopLeft) {
        int w = image.getWidth(), h = image.getHeight();
        int[] argb = image.getRGB(0, 0, w, h, null, 0, w);
        IntBuffer buf = BufferUtils.createIntBuffer(w * h);
        for (int y = 0; y < h; y++) {
            buf.put(argb, (h - 1 - y) * w, w);
        }
        buf.flip();
        setCursor(w, h, xHot, h - 1 - yHotTopLeft, buf);
    }

    public static void reset() {
        try {
            Mouse.setNativeCursor(null);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }
}
