package me.committee.impl.hud;

import me.committee.Committee;
import me.committee.api.gui.hud.elements.HudElement;
import me.committee.api.setting.Colour;
import me.committee.api.util.RenderUtil;
import me.committee.api.util.ThemedRenderUtil;
import me.committee.impl.modules.client.HudOverlay;
import me.xenforu.kelo.util.font.Fonts;

import java.awt.*;

public class Watermark extends HudElement {

    public Watermark() {
        super("Watermark", new String[]{},"Renders a watermark.", 10, 30, 0, 0);
    }

    private int time = 0;

    @Override
    public void render(float partialTicks) {
        final String text = Committee.MOD_NAME + " \u2503 v" + Committee.VERSION_NUMBER;

        final int stringWidth = mc.fontRenderer.getStringWidth(text);
        final int stringHeight = mc.fontRenderer.FONT_HEIGHT;

        final int topHeight = 3;

        this.setWidth(stringWidth + 10);
        this.setHeight(stringHeight + 7);

        RenderUtil.drawRect(this.getX(), this.getY(), this.getWidth(), topHeight, Color.getHSBColor((float) ((Math.ceil((float) (this.time) / 20) % 360) / 360), 0.8f, 0.7f).getRGB());
        ThemedRenderUtil.drawRectangle(this.getX(), this.getY() + topHeight, this.getWidth(), this.getHeight() - topHeight);

        final Colour colour;
        colour = (Boolean) Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(16).getValue()
                ? (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(0).getValue()
                : (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(15).getValue();
        mc.fontRenderer.drawString(
                text,
                (int)(this.getX() + this.getWidth() /2 - mc.fontRenderer.getStringWidth(text)/ 2),
                (int)(this.getY() + mc.fontRenderer.FONT_HEIGHT * 0.7),
                colour.hashCode());
        this.time++;

    }

}
