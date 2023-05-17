package me.committee.impl.hud;

import me.committee.Committee;
import me.committee.api.gui.hud.elements.HudElement;
import me.committee.api.setting.Colour;
import me.committee.impl.modules.client.HudOverlay;
import net.minecraft.client.Minecraft;

public class FPS extends HudElement {

    public FPS() {
        super("FPS", new String[]{}, "shows FPS", 5, 60, 0, 0);
    }

    @Override
    public void render(float partialTicks) {
        if(mc.player == null)
            return;

        final String text;
        text = " "+ Minecraft.getDebugFPS() + "\u00a7l fps\u00a7r";

        this.setWidth(mc.fontRenderer.getStringWidth(text));
        this.setHeight(mc.fontRenderer.FONT_HEIGHT);

        final Colour colour;
        colour = (Boolean) Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(11).getValue()
                ? (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(0).getValue()
                : (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(12).getValue();
        mc.fontRenderer.drawString
                (text,
                        (int) this.getX(),
                        (int) this.getY(),
                        colour.hashCode());
    }
}

