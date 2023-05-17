package me.committee.impl.hud;

import me.committee.Committee;
import me.committee.api.gui.hud.elements.HudElement;
import me.committee.api.setting.Colour;
import me.committee.impl.modules.client.HudOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;

public class Coords extends HudElement {


    public Coords() {
        super("Coords", new String[]{}, "coordinates", 10, 10, 0, 0);
    }

  
    @Override
    public void render(float partialTicks) {
        if (mc.player == null)
            return;

        final double posX = mc.player.posX;
        final double posY = mc.player.posY;
        final double posZ = mc.player.posZ;

        String text;
        if ((Boolean) Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(6).getValue()) {
            if (mc.player.dimension == -1) {
                text =
                        " " + String.format("\u00a7c%.1f", posX) + "\u00a77 (\u00a7r\u00a7l" + String.format("%.1f", posX * 8) + "\u00a7r\u00a77)\u00a7r" + "\u00a77,\u00a7r " +
                                String.format("\u00a7c%.1f", posY) + "\u00a77,\u00a7r " +
                                String.format("\u00a7c%.1f", posZ) + "\u00a77 (\u00a7r\u00a7l" + String.format("%.1f", posZ * 8) + "\u00a7r\u00a77)\u00a7r" + " \u00a77XYZ\u00a7r";
            } else {
                text =
                        " " + String.format("%.1f", posX) + "\u00a77 (\u00a7c\u00a7l" + String.format("%.1f", posX / 8) + "\u00a7r\u00a77)\u00a7r" + "\u00a77,\u00a7r " +
                                String.format("%.1f", posY) + "\u00a77,\u00a7r " +
                                String.format("%.1f", posZ) + "\u00a77 (\u00a7c\u00a7l" + String.format("%.1f", posZ / 8) + "\u00a7r\u00a77)\u00a7r" + " \u00a77XYZ\u00a7r";
            }
        } else {
            if (mc.player.dimension == -1) {
                text =
                        " " + String.format("%.1f", posX) + "\u00a77 (\u00a7r\u00a7l" + String.format("%.1f", posX * 8) + "\u00a7r\u00a77)\u00a7r" + "\u00a77,\u00a7r " +
                                String.format("%.1f", posY) + "\u00a77)\u00a7r" + "\u00a77,\u00a7r " +
                                String.format("%.1f", posZ) + "\u00a77 (\u00a7r\u00a7l" + String.format("%.1f", posZ * 8) + "\u00a7r\u00a77)\u00a7r" + " \u00a77XYZ\u00a7r";
            } else {
                text =
                        " " + String.format("%.1f", posX) + "\u00a77 (\u00a7r\u00a7l" + String.format("%.1f", posX / 8) + "\u00a7r\u00a77)\u00a7r" + "\u00a77,\u00a7r " +
                                String.format("%.1f", posY) + "\u00a77,\u00a7r " +
                                String.format("%.1f", posZ) + "\u00a77 (\u00a7r\u00a7l" + String.format("%.1f", posZ / 8) + "\u00a7r\u00a77)\u00a7r" + " \u00a77XYZ\u00a7r";
            }
        }

        this.setWidth(mc.fontRenderer.getStringWidth(text));
        this.setHeight(mc.fontRenderer.FONT_HEIGHT);

        final Colour colour;
        colour = (Boolean) Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(4).getValue()
                ? (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(0).getValue()
                : (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(5).getValue();
        mc.fontRenderer.drawString
                (text,
                        (int) this.getX(),
                        (int) this.getY(),
                        colour.hashCode());
    }
}
