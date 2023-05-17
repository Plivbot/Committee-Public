package me.committee.impl.hud;

import me.committee.Committee;
import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.gui.hud.elements.HudElement;
import me.committee.api.setting.Colour;
import me.committee.api.util.RenderUtil;
import me.committee.api.util.StopWatch;
import me.committee.api.util.ThemedRenderUtil;
import me.committee.impl.event.events.network.ReceivePacketEvent;
import me.committee.impl.modules.client.HudOverlay;

public class LagNotifier extends HudElement {

    public LagNotifier() {
        super("LagNotifier", new String[]{}, "notifies u of lag", 10, 70, 0, 0);
    }

    private final StopWatch stopWatch = new StopWatch();

    @EventSubscribe
    public void receivePacket(ReceivePacketEvent event) {
        stopWatch.reset();
    }

    @Override
    public void render(float partialTicks) {

        final String text;
        text = stopWatch.toSeconds(1) > 1.0 ? "Server Not Responding For " + stopWatch.toSeconds(1) + " s"
                : ((Boolean) Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(21).getValue()? "Server Not Responding For 1.0 s" : "");
    /* ^ this based on this, I made it work and better cuz tff is retarded -Pliv
    if (stopWatch.current() > 0.5) {
            mc.fontRenderer.drawString("Server Not Responding For" + stopWatch.current(),
                    (int) (this.getX() + this.getWidth() / 2 - mc.fontRenderer.getStringWidth(String.valueOf(stopWatch.current())) / 2),
                    (int) (this.getY() + mc.fontRenderer.FONT_HEIGHT * 0.7),
                    -1);
        };
     */

        this.setWidth(mc.fontRenderer.getStringWidth(text));
        this.setHeight(mc.fontRenderer.FONT_HEIGHT);

        final Colour colour;
        colour = (Boolean) Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(20).getValue()
                ? (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(0).getValue()
                : (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(19).getValue();
        mc.fontRenderer.drawString
                (text,
                        (int) this.getX(),
                        (int) this.getY(),
                        colour.hashCode());
    }
}
