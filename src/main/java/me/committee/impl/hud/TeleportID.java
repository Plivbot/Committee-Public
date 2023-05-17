package me.committee.impl.hud;

import me.committee.Committee;
import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.gui.hud.elements.HudElement;
import me.committee.api.setting.Colour;
import me.committee.impl.event.events.network.ReceivePacketEvent;
import me.committee.impl.modules.client.HudOverlay;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class TeleportID extends HudElement {
    public TeleportID() {
        super("TeleportID", new String[]{}, "Shows the current teleport ID.", 10, 90);
    }

    int lastTeleportId = 0;

    @Override
    public void render(float partialTicks) {
        final String text = "Teleport id: " + lastTeleportId;
        final int width = mc.fontRenderer.getStringWidth(text);
        final int height = mc.fontRenderer.FONT_HEIGHT;

        setWidth(width);
        setHeight(height);

        final Colour colour;
        colour = (Boolean) Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(13).getValue()
                ? (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(0).getValue()
                : (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(14).getValue();
        mc.fontRenderer.drawString
                (text,
                        (int) this.getX(),
                        (int) this.getY(),
                        colour.hashCode());
    }

    @EventSubscribe
    public void onPacket(ReceivePacketEvent event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            lastTeleportId = ((SPacketPlayerPosLook) event.getPacket()).getTeleportId();
        }
    }
}
