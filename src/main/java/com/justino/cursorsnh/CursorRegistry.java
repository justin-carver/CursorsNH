package com.justino.cursorsnh;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CursorRegistry {
    public static class Entry {
        public BufferedImage image;
        public int xHot, yHot;      // top-left origin, pack convention
        public int frameCount = 1;
        public int frameTime = 1;
        public String animationMode = "loop";
        public float scale = 1.0f;
        public boolean enabled = true;
    }

    private static final Map<String, Entry> entries = new HashMap<>();

    public static void reload(CursorSource source) {
        entries.clear();
        for (String name : source.listCursors()) {
            try {
                byte[] png = source.getCursorImage(name);
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(png));
                if (image == null) {
                    CursorsNH.LOG.error("No decoder for cursor: " + name);
                    continue;
                }

                Entry entry = new Entry();
                entry.image = image;

                // sidecar, frame count, cropping go here

                entries.put(name, entry);
            } catch (IOException e) {
                CursorsNH.LOG.error("Failed to read cursor: " + name, e);
            }
        }
        dump();
    }

    public static Entry get(String name) { return entries.get(name); }

    // DEBUG
    private static void dump() {
        // Log every entry: name, dimensions, hotspot, frameCount, etc.
        CursorsNH.LOG.info("=== Cursor registry: " + entries.size() + " loaded ===");
        for (Map.Entry<String, Entry> e : entries.entrySet()) {
            Entry c = e.getValue();
            CursorsNH.LOG.debug(String.format(
                "  %-14s %dx%d  hot=(%d,%d)  frames=%d",
                e.getKey(),
                c.image.getWidth(), c.image.getHeight(),
                c.xHot, c.yHot,
                c.frameCount));
        }
    }
}
