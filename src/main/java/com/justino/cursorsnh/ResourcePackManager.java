package com.justino.cursorsnh;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.compress.utils.IOUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ResourcePackManager implements CursorSource, IResourceManagerReloadListener {

    public static final String[] CURSOR_NAMES = {
        "default", "pointing_hand", "ibeam", "crosshair", "grabbing", "shift",
        "busy", "not_allowed", "resize_all", "resize_ew", "resize_ns",
        "resize_nwse", "resize_nesw"
    };

    // v4 first, then v3 fallback
    private static final String[][] LAYOUTS = {
        { "cursors_extended", "textures/gui/sprites/cursors/" },
        { "minecraft-cursor", "textures/cursors/" }
    };

    private IResourceManager manager;

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.manager = resourceManager;
        CursorRegistry.reload(this);   // rebuild everything downstream
    }

    /** Reads a resource, returning null if no enabled pack provides it. */
    private byte[] read(String domain, String path) {
        try {
            IResource res = manager.getResource(new ResourceLocation(domain, path));
            return IOUtils.toByteArray(res.getInputStream());
        } catch (IOException e) {
            return null;   // pack doesn't provide it
        }
    }

    @Override
    public byte[] getCursorImage(String name) {
        for (String[] layout : LAYOUTS) {
            byte[] data = read(layout[0], layout[1] + name + ".png");
            if (data != null) return data;
        }
        return null;
    }

    @Override
    public byte[] getCursorMetadata(String name) {
        for (String[] layout : LAYOUTS) {
            // Only read the sidecar from the layout that actually has the image,
            // so a v3 sidecar can't attach to a v4 image.
            if (read(layout[0], layout[1] + name + ".png") == null) continue;
            return read(layout[0], layout[1] + name + layout[2]);
        }
        return null;
    }

    @Override
    public Set<String> listCursors() {
        Set<String> found = new HashSet<>();
        for (String name : CURSOR_NAMES) {
            if (getCursorImage(name) != null) found.add(name);
        }
        return found;
    }
}
