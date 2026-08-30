package com.justino.cursorsnh;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.client.ClientCommandHandler;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());

        CursorsNH.LOG.info(Config.greeting);
        CursorsNH.LOG.info("CursorsNH currently loading in at via " + Tags.VERSION);
        CursorsNH.LOG.info("What LWJGL version am I?: " + org.lwjgl.Sys.getVersion());
        if (org.lwjgl.Sys.getVersion().charAt(0) == '3') {
            CursorsNH.LOG.info("Running LWJGL3ify!");
        } else {
            CursorsNH.LOG.error("Something is missing... Are we running JDK 17+?");
        }

        // Command Registration
        ClientCommandHandler.instance.registerCommand(new CursorTestCommand());
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {

        // Load Images
        TexturePackLoader.loadSingleCursor("/assets/cursorsnh/textures/DEV-default.png", 0, 0);
    }

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
    }
}
