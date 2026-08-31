package com.justino.cursorsnh;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static String greeting = "Comin' to ya live from pre-init!";
    public static boolean cursorNative = false;   // false = draw the cursor in-game
    public static float cursorScale = 1.0f;

    private static Configuration configuration;
    private static File file;

    public static void synchronizeConfiguration(File configFile) {
        file = configFile;
        configuration = new Configuration(configFile);
        configuration.load();

        // Mod-related configs
        greeting = configuration.getString("greeting", Configuration.CATEGORY_GENERAL, greeting, "Script initialization lingo");

        // Cursor-related configs
        cursorNative = configuration.getBoolean(
            "native",
            Configuration.CATEGORY_GENERAL,
            cursorNative,
            "Use OS-level cursors. Disable to draw the cursor in-game (allows larger sizes/scales).");

        cursorScale = (float) configuration.get(
            Configuration.CATEGORY_GENERAL,
            "scale",
            1.0D,
            "Cursor scale multiplier. Only fully effective when 'native' is false.",
            0.5D,
            8.0D).getDouble();

        // Ship it!
        if (configuration.hasChanged()) {
            save();
        }
    }

    public static void save() {
        // TODO: Let's make this extensible from the beginning... iterate over list/tuple?
        configuration.get(Configuration.CATEGORY_GENERAL, "native", true).set(cursorNative);
        configuration.get(Configuration.CATEGORY_GENERAL, "cursorScale", 1.0D).set((double) cursorScale);
        configuration.save();
    }

    public static void reload() {
        synchronizeConfiguration(file);
    }
}
