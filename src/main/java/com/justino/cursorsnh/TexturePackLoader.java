package com.justino.cursorsnh;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class TexturePackLoader {
    public static void loadSingleCursor(String resourcePath, int xHot, int yHot) {
        try (InputStream in = TexturePackLoader.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                CursorsNH.LOG.error("Resource not found: " + resourcePath);
                return;
            }

            BufferedImage image = ImageIO.read(in);
            if (image == null) {
                CursorsNH.LOG.error("No image reader for: " + resourcePath);
                return;
            }

            int width = image.getWidth();
            int height = image.getHeight();
            CursorsNH.LOG.info("Loaded " + resourcePath + " " + width + "x" + height);

            // Bulk-read the whole image as packed ARGB ints, top row first.
            int[] argb = image.getRGB(0, 0, width, height, null, 0, width);

            IntBuffer buffer = BufferUtils.createIntBuffer(width * height);

            for (int y = 0; y < height; y++) {
                int srcRow = height - 1 - y; // bottom-up
                buffer.put(argb, srcRow * width, width); // one whole row at a time
            }

            buffer.flip();
            // CursorsNH.LOG.info("Remaining buffer: " + buffer.remaining());

            CursorNative.setCursor(width, height, xHot, height - 1, buffer);

        } catch (IOException e) {
            CursorsNH.LOG.error("Error reading " + resourcePath);
            e.printStackTrace();
        }
    }
}
