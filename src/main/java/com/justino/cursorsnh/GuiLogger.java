package com.justino.cursorsnh;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 *  THIS IS ONLY A DEBUGGING CLASS.
 *  SHOULD NOT BE ADDED INTO THE FINAL BUILD!!!
 *  DON'T MIND THE MESS. :)
 * */
public class GuiLogger {

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        GuiScreen gui = event.gui;
        if (gui == null) {
            System.out.println("[CursorsNH] GUI closed");
        } else {
            System.out.println(
                "[CursorsNH] Gui opened: " + gui.getClass()
                    .getName());
        }
    }
}
