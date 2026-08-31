package com.justino.cursorsnh;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

public class CursorNative {

    private enum State { SYSTEM, CUSTOM, HIDDEN }
    private static State state = State.SYSTEM;
    private static Cursor current;
    private static Cursor blank;

    /** Installs a pack cursor, replacing and destroying any previous one. */
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

    /** Hide the OS pointer behind a fully transparent cursor. Safe to call repeatedly. */
    public static void hide() {
        if (state == State.HIDDEN) {
            return;
        }
        Cursor b = blankCursor();
        if (b == null) {
            return;
        }
        try {
            Mouse.setNativeCursor(b);
            state = State.HIDDEN;
        } catch (LWJGLException e) {
            CursorsNH.LOG.error("Could not hide the OS native cursor!", e);
        }
    }

    /** Restores the OS default pointer. Safe to call repeatedly. */
    public static void reset() {
        if (state == State.SYSTEM) {
            return;
        }
        try {
            if (current != null) {
                current.destroy();
                current = null;
            }
            Mouse.setNativeCursor(null);
            state = State.SYSTEM;
        } catch (LWJGLException e) {
            CursorsNH.LOG.error("Couldn't reset the cursor back to native!", e);
        }
    }

    /** Constructs Cursor parameters and assigns Cursor object manually */
    private static void setCursor(int width, int height, int xHot, int yHot, IntBuffer argb) {
        int yHotFromPack = 0; // Debug value for now.
        int yHotShim = height - 1 - yHotFromPack;

        try {
            Cursor next = new Cursor(width, height, xHot, yHotShim, 1, argb, null);
            Mouse.setNativeCursor(next);

            if (current != null) {
                current.destroy();
            }
            current = next;
            state = State.CUSTOM;
        } catch (LWJGLException e) {
            CursorsNH.LOG.error("Could not set the cursor properly at all...", e);
        }
    }

    /** 32x32 fully transparent cursor, created on first use. */
    private static Cursor blankCursor() {
        if (blank != null) {
            return blank;
        }
        try {
            IntBuffer intBuff = BufferUtils.createIntBuffer(32*32);
            blank = new Cursor(32, 32, 0, 0, 1, intBuff, null);
        } catch (LWJGLException e) {
            CursorsNH.LOG.error("Failed to create transparent cursor.", e);
        }
        return blank;
    }
}
