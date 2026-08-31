package com.justino.cursorsnh;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        // Client-only stuff
        MinecraftForge.EVENT_BUS.register(new GuiLogger());
        MinecraftForge.EVENT_BUS.register(new VirtualCursorRenderer());

        // Resource Pack stuff
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager())
            .registerReloadListener((IResourceManagerReloadListener) new ResourcePackManager());
    }
}
