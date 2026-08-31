package com.justino.cursorsnh;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config {

    public static String greeting = "Comin' to ya live from pre-init!";
    public static boolean cursorNative = false;   // false = draw the cursor in-game
    public static float cursorScale = 1.0f;

    private static Configuration configuration;
    private static File file;

    private static final float SCALE_MIN = 0.4f;
    private static final float SCALE_MAX = 4.0f;
    private static Property propNative;
    private static Property propScale;

    public static void synchronizeConfiguration(File configFile) {
        file = configFile;
        configuration = new Configuration(configFile);
        configuration.load();

        // Mod-related configs
        greeting = configuration.getString("greeting", Configuration.CATEGORY_GENERAL, greeting, "Script initialization lingo");

        // Cursor-related configs
        propNative = configuration.get(Configuration.CATEGORY_GENERAL, "native", cursorNative, "Use OS-level cursors. Disable to draw the cursor in-game (allows larger sizes/scales).");
        cursorNative = propNative.getBoolean();

        propScale = configuration.get(Configuration.CATEGORY_GENERAL, "scale", cursorScale, "Cursor scale multiplier. Only fully effective when 'native' is false.");
        cursorScale = Math.clamp((float) propScale.getDouble(), SCALE_MIN, SCALE_MAX);

        // Ship it!
        if (configuration.hasChanged()) {
            save();
        }
    }

    public static void save() {
        // TODO: Let's make this extensible from the beginning... iterate over list/tuple?
        propNative.set(cursorNative);
        propScale.set(cursorScale);
        configuration.save();
        reload(); // Load the immediate config to present any breaking changes.
    }

    public static void reload() {
        synchronizeConfiguration(file);
    }
}
