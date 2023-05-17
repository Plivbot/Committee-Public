package me.committee.impl.hud;

import me.committee.Committee;
import me.committee.api.gui.hud.elements.HudElement;
import me.committee.api.mixin.mixins.accessors.AccessorMinecraft;
import me.committee.api.mixin.mixins.accessors.AccessorTimer;
import me.committee.api.setting.Colour;
import me.committee.impl.modules.client.HudOverlay;
import net.minecraft.util.math.MathHelper;

public class Speed extends HudElement {

    public Speed() {
        super("Speed", new String[]{}, "measures speed", 5, 80, 0, 0);
    }


    @Override
    public void render(float partialTicks) {
        if(mc.player == null)
            return;
        final double changeX = mc.player.posX - mc.player.prevPosX;
        final double changeZ = mc.player.posZ - mc.player.prevPosZ;
        final float time = (((AccessorTimer) ((AccessorMinecraft) mc).getTimer()).getTickLength() / 1000.0f);

        final String text;
        text =
                " " + String.format("%.2f", MathHelper.sqrt(changeX * changeX + changeZ * changeZ) / time) + "\u00a7l bps\u00a7r";

        this.setWidth(mc.fontRenderer.getStringWidth(text));
        this.setHeight(mc.fontRenderer.FONT_HEIGHT);

            final Colour colour;
            colour = (Boolean) Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(7).getValue()
                    ? (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(0).getValue()
                    : (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(8).getValue();
            mc.fontRenderer.drawString
                    (text,
                            (int) this.getX(),
                            (int) this.getY(),
                            colour.hashCode());
    }
}
