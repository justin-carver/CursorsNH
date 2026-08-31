package com.justino.cursorsnh;

import java.util.Set;

// TODO: Generate JavaDocs more aggressively, for future API needs...
public interface CursorSource {
    /** @return raw PNG bytes, or null if this source doesn't have it */
    byte[] getCursorImage(String name);

    /** @return raw sidecar JSON bytes, or null if absent (normal, not an error) */
    byte[] getCursorMetadata(String name);

    /** @return names of cursors this source provides */
    Set<String> listCursors();
}
