package me.committee.impl.hud;

import me.committee.Committee;
import me.committee.api.gui.hud.elements.HudElement;
import me.committee.api.setting.Colour;
import me.committee.impl.modules.client.HudOverlay;

public class Welcomer extends HudElement {
    public Welcomer() {
        super("Welcomer", new String[]{"Halloer"},"Says ur a fucking idiot", 10, 50, 0, 0);
    }

    @Override
    public void render(float partialticks) {
         final String text = ((String)
             Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(2).getValue())
                 .replace("%p", mc.player.getName())
                 .replace("Presidentt005", "Idiot");

         this.setWidth(mc.fontRenderer.getStringWidth(text));
         this.setHeight(mc.fontRenderer.FONT_HEIGHT);

        final Colour colour;
        colour = (Boolean) Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(1).getValue()
                ? (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(0).getValue()
                : (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(3).getValue();
        mc.fontRenderer.drawString
                (text,
                        (int) this.getX(),
                        (int) this.getY(),
                        colour.hashCode());
    }
}
