package com.justino.cursorsnh;

import net.minecraft.command.ICommand;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        // Client-only stuff
        MinecraftForge.EVENT_BUS.register(new GuiLogger());
        MinecraftForge.EVENT_BUS.register(new VirtualCursorRenderer());
    }

    public void refreshCursor() {

    }
}
