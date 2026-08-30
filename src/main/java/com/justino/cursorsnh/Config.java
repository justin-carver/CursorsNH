package com.justino.cursorsnh;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static String greeting = "Comin' to ya live from pre-init!";
    public static boolean cursorNative = false;   // false = draw the cursor in-game
    public static float cursorScale = 1.0f;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);
        configuration.load();

        greeting = configuration.getString("greeting", Configuration.CATEGORY_GENERAL, greeting, "Script initialization lingo");

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

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
