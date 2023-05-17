package me.committee.impl.hud;

import me.committee.Committee;
import me.committee.api.gui.hud.elements.HudElement;
import me.committee.api.setting.Colour;
import me.committee.impl.modules.client.HudOverlay;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time extends HudElement {

    public Time() {
        super("Time", new String[]{}, "Displays Time", 5, 100, 0, 0);
    }


    /*
    private void methodNameHere() {
      Code Here...
    }
    */

    private String time() {
        return new SimpleDateFormat("hh:mm").format(new Date());
    }
    private String am() {
        return new SimpleDateFormat(" a").format(new Date());
    }

    @Override
    public void render(float partialTicks) {
        if(mc.player == null)
            return;

        final String text;
        text = " "+ time() + "\u00a7l" + am() + "\u00a7r";

        this.setWidth(mc.fontRenderer.getStringWidth(text));
        this.setHeight(mc.fontRenderer.FONT_HEIGHT);

        final Colour colour;
        colour = (Boolean) Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(9).getValue()
                ? (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(0).getValue()
                : (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(10).getValue();
        mc.fontRenderer.drawString
                (text,
                        (int) this.getX(),
                        (int) this.getY(),
                        colour.hashCode());
    }
}