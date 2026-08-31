package com.justino.cursorsnh;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

public class ResourcePackManager implements CursorSource, IResourceManagerReloadListener {

    /**
     * Combining both Standard + Extra cursors for consistency.
     * Also including unused cursors, in the event that someone would like to extend them
     * in MUI2 or other GUI libraries in the future.
     * */
    public static final String[] CURSOR_NAMES = {
        "default", "pointing_hand", "ibeam", "crosshair", "grabbing", "shift",
        "busy", "not_allowed", "resize_all", "resize_ew", "resize_ns",
        "resize_nwse", "resize_nesw"
    };

    /** v3 packs name two of the cursors differently from the canonical vocabulary. */
    private static final Map<String, String> V3_ALIASES;
    static {
        V3_ALIASES = Map.of("pointing_hand", "pointer", "ibeam", "text");
    }

    /** v4 first, then v3 fallback. */
    private static final Layout[] LAYOUTS = {
        new Layout("cursors_extended", "textures/gui/sprites/cursors/", Collections.emptyMap()),
        new Layout("minecraft-cursor", "textures/cursors/", V3_ALIASES)
    };

    /** Tried in order, but a pack may use either. */
    private static final String[] SIDECAR_EXTENSIONS = { ".png.json", ".png.mcmeta" };

    private IResourceManager manager;

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.manager = resourceManager;
        CursorRegistry.reload(this);   // rebuild everything downstream
        CursorManager.applyDefault();
    }

    /** Reads a resource, returning null if no enabled pack provides it. */
    private byte[] read(String domain, String path) {
        try {
            IResource res = manager.getResource(new ResourceLocation(domain, path));
            try {
                return IOUtils.toByteArray(res.getInputStream());
            } catch (IOException e) {
                CursorsNH.LOG.error("Found the resource pack at file: {}, but could not read from it. Is it corrupt?", domain + path, e);
            }
        } catch (IOException e) {
            return null;   // pack doesn't provide it
        }
        return null;
    }

    @Override
    public byte[] getCursorImage(String name) {
        for (Layout layout : LAYOUTS) {
            byte[] data = read(layout.domain(), layout.dir() + layout.fileName(name) + ".png");
            if (data != null) return data;
        }
        return null;
    }

    @Override
    public byte[] getCursorMetadata(String name) {
        for (Layout layout : LAYOUTS) {
            String base = layout.dir() + layout.fileName(name);
            // Only read the sidecar from the layout that actually has the image,
            // so a v3 sidecar can't attach to a v4 image.
            if (read(layout.domain(), base + ".png") == null) continue;
            for (String extension : SIDECAR_EXTENSIONS) {
                byte[] data = read(layout.domain(), base + extension);
                if (data != null) return data;
            }
            return null;
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

    private record Layout(String domain, String dir, Map<String, String> aliases) {
        // We need to map domains and paths for v3/v4 packs
        String fileName(String canonical) {
            String alias = aliases.get(canonical);
            return alias == null ? canonical : alias;
        }
    }
}


