package com.justino.cursorsnh;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import java.nio.IntBuffer;

public class CursorNative {

    private static Cursor current;

    public static void setCursor(int width, int height, int xHot, int yHot, IntBuffer argb) {
        try {
            Cursor next = new Cursor(width, height, xHot, yHot, 1, argb, null);
            Mouse.setNativeCursor(next);

            if (current != null) {
                current.destroy();
            }
            current = next;
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public static void reset() {
        try {
            Mouse.setNativeCursor(null);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }
}
