package com.justino.cursorsnh;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

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

            CursorsNH.LOG.info("Loaded " + resourcePath + " "
                + image.getWidth() + "x" + image.getHeight());

            CursorManager.setCursor(image, xHot, yHot);

        } catch (IOException e) {
            CursorsNH.LOG.error("Error reading " + resourcePath);
            e.printStackTrace();
        }
    }
}
