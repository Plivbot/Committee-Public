package me.committee.impl.modules.client;

import me.committee.Committee;
import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.feature.ToggleableFeature;
import me.committee.api.module.Module;
import me.committee.api.setting.Colour;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.render.Render2DEvent;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class HudOverlay extends Module {

    public HudOverlay() {
        super("HudOverlay", new String[]{"Hud", "H"}, "Ingame Overlay.", Category.CLIENT, true);
    }

    // used for syncing colors
    private final Setting<Colour> syncColour = new Setting<>("SyncColour", new String[]{"SC"}, "Syncs colours", new Colour(245, 168, 184)).setID(0);
    // this is used in Welcomer
    private final Setting<Boolean> welcomerSync = new Setting<>("WelcomerSync", new String[]{"WS"}, "Toggles colour sync", false).setID(1);
    private final Setting<String> welcomerText = new Setting<>("WelcomerText", new String[]{"WT"}, "The String \"%p\" gets replaced with the player's name", "Whatsup you fucking idiot %p!").setID(2);
    private final Setting<Colour> welcomerColour = new Setting<>("WelcomerColour", new String[]{"WC"}, "Colour used for Welcomer text", new Colour(255, 255, 255)).setID(3);
    // this is for coords
    private final Setting<Boolean> coordsSync = new Setting<>("CoordsSync", new String[]{"CS"}, "Toggles colour sync", false).setID(4);
    private final Setting<Colour> coordsColour = new Setting<>("CoordsColour", new String[]{"CC"}, "Colour used for Coords text", new Colour(255, 255, 255)).setID(5);
    private final Setting<Boolean> netherInRed = new Setting<>("NetherInRed", new String[]{"R", "NIR"}, "Colours nether Coords red", false).setID(6);
    // this is for speed
    private final Setting<Boolean> speedSync = new Setting<>("SpeedSync", new String[]{"SS"}, "Toggles colour sync", false).setID(7);
    private final Setting<Colour> speedColour = new Setting<>("SpeedColour", new String[]{"SPC"}, "Colour used for Speed text", new Colour(255, 255, 255)).setID(8);
    // this is for time
    private final Setting<Boolean> timeSync = new Setting<>("TimeSync", new String[]{"TS"}, "Toggles colour sync", false).setID(9);
    private final Setting<Colour> timeColour = new Setting<>("TimeColour", new String[]{"TC"}, "Colour used for Time text", new Colour(255, 255, 255)).setID(10);
    // this is for FPS
    private final Setting<Boolean> fpsSync = new Setting<>("FPSSync", new String[]{"FS"}, "Toggles colour sync", false).setID(11);
    private final Setting<Colour> fpsColour = new Setting<>("FPSColour", new String[]{"FC"}, "Colour used for FPS text", new Colour(255, 255, 255)).setID(12);
    // this is for TeleportID
    private final Setting<Boolean> teleportIDSync = new Setting<>("TeleportIDSync", new String[]{"TIS"}, "Toggles colour sync", false).setID(13);
    private final Setting<Colour> teleportIDColour = new Setting<>("TeleportIDColour", new String[]{"TIC"}, "Colour used for TeleportID text", new Colour(255, 255, 255)).setID(14);
    // this is for Watermark
    private final Setting<Colour> watermarkColour = new Setting<>("WatermarkColour", new String[]{"WMC"}, "Colour used for Watermark text", new Colour(255, 255, 255)).setID(15);
    private final Setting<Boolean> watermarkSync = new Setting<>("WatermarkSync", new String[]{"WMS"}, "Toggles colour sync", false).setID(16);
    // this is for ModuleList
    private final Setting<Colour> arrayListColour = new Setting<>("ArrayListColour", new String[]{"AC"}, "Colour used for ArrayList text", new Colour(255, 255, 255)).setID(17);
    private final Setting<Boolean> arrayListSync = new Setting<>("ArrayListSync", new String[]{"AS"}, "Toggles colour sync", false).setID(18);
    //this is for LagNotifier
    private final Setting<Colour> lagNotifierColour = new Setting<>("LagNotifierColour", new String[]{"LC"}, "Colour used for LagNotifierColour text", new Colour(255, 255, 255)).setID(19);
    private final Setting<Boolean> lagNotifierSync = new Setting<>("LagNotifierSync", new String[]{"LS"}, "Toggles colour sync", false).setID(20);
    private final Setting<Boolean> lagNotifierTextPlaceHolderForPlacement = new Setting<>("LagNotifierTextPlaceHolderForPlacement", new String[]{"LP"}, "Used for placing The LagNotifier", true).setID(21);
    //this is for packetsent
    private final Setting<Colour> packetSentColour = new Setting<>("packetSentColour", new String[]{"PC"}, "Colour used for PacketSent text", new Colour(255, 255, 255)).setID(22);
    private final Setting<Boolean> packetSentSync = new Setting<>("packetSentSync", new String[]{"PS"}, "Toggles colour sync", false).setID(23);

    @EventSubscribe
    public void onRender2d(Render2DEvent event) {

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();

        if (mc.currentScreen != Committee.committeeHudEditor) {
            Committee.hudElementManager.getElements()
                    .stream()
                    .filter(ToggleableFeature::isEnabled)
                    .forEach(hudElement -> hudElement.render(event.getPartialTicks()));

        }
        /*
        final String text = "none of us can code we are all skids";
        final ScaledResolution sr = new ScaledResolution(mc);
        final double centerX = sr.getScaledWidth() / 2;
        final double centerY = sr.getScaledHeight() / 2;
        mc.fontRenderer.drawString(text,(int)centerX-(mc.fontRenderer.getStringWidth(text)/2), (int)centerY-(mc.fontRenderer.FONT_HEIGHT /2), Color.white.hashCode() );

         */

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();


    }

}

