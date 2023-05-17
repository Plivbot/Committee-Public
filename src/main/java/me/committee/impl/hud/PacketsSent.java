package me.committee.impl.hud;

import me.committee.Committee;
import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.gui.hud.elements.HudElement;
import me.committee.api.setting.Colour;
import me.committee.impl.event.events.network.SendPacketEvent;
import me.committee.impl.modules.client.HudOverlay;

import java.util.HashSet;

public class PacketsSent extends HudElement {

    public PacketsSent() {
        super("PacketsSent", new String[]{}, "tells u how many packets ur sending per 250ms", 10, 20, 0, 0);
    }
    private HashSet<Long> packets = new HashSet<>();

    @EventSubscribe
    public void onSendPacket(SendPacketEvent event) {
        if (event.getPacket() != null) {
            packets.add(System.currentTimeMillis());
            packets.removeIf(p -> System.currentTimeMillis() - p > 500);
        }

    }

    @Override
    public void render(float partialTicks) {

        final String text;
        text = "packets " + packets.size();

        this.setWidth(mc.fontRenderer.getStringWidth(text));
        this.setHeight(mc.fontRenderer.FONT_HEIGHT);

        final Colour colour;
        colour = (Boolean) Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(23).getValue()
                ? (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(0).getValue()
                : (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(22).getValue();

        mc.fontRenderer.drawString(
                text,
                (int)this.getX(),
                (int)this.getY(),
                colour.hashCode()
        );
    }
}
