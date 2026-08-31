package com.justino.cursorsnh;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;

public class VirtualCursorRenderer {
    private static final ResourceLocation LOC =
        new ResourceLocation("cursorsnh", "dynamic/cursor");

    private static DynamicTexture texture;
    private static int texW, texH, xHot, yHot;
    private static boolean visible = false;

    public static void show(BufferedImage image, int xHotTopLeft, int yHotTopLeft) {
        if (texture != null) {
            Minecraft.getMinecraft().getTextureManager().deleteTexture(LOC);
        }
        texture = new DynamicTexture(image);
        Minecraft.getMinecraft().getTextureManager().loadTexture(LOC, texture);
        texW = image.getWidth();
        texH = image.getHeight();
        xHot = xHotTopLeft;
        yHot = yHotTopLeft;
        visible = true;

        CursorNative.setBlank();   // hide the OS cursor
    }

    public static void hide() {
        visible = false;
    }

    @SubscribeEvent
    public void onDrawPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        // CursorsNH.LOG.info("draw: visible=" + visible + " texture=" + (texture != null));
        if (!visible || texture == null) return;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        float scale = Config.cursorScale;

        // Mouse position in GUI space, read fresh for minimum lag.
        int mouseX = org.lwjgl.input.Mouse.getX() * sr.getScaledWidth() / mc.displayWidth;
        int mouseY = sr.getScaledHeight()
            - org.lwjgl.input.Mouse.getY() * sr.getScaledHeight() / mc.displayHeight - 1;

        float x = mouseX - xHot * scale;
        float y = mouseY - yHot * scale;
        float w = texW * scale;
        float h = texH * scale;

        mc.getTextureManager().bindTexture(LOC);

        // Crisp pixels instead of blurry upscaling.
        // TODO: Need to test this with larger screens. 2K, 4K, 8K, high-dpi, etc.
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glTranslatef(0f, 0f, 500f);   // draw above everything

        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV(x,     y + h, 0, 0, 1);
        t.addVertexWithUV(x + w, y + h, 0, 1, 1);
        t.addVertexWithUV(x + w, y,     0, 1, 0);
        t.addVertexWithUV(x,     y,     0, 0, 0);
        t.draw();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }
}
