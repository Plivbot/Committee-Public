package me.committee.impl.modules.render;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.player.PlayerUpdateEvent;

public class FullBright extends Module {

    private float oldGamma;

    public FullBright() {
        super("FullBright", new String[]{"FB", "Brightness"}, "Fulls your brigh.t", Category.RENDER);
    }

    private final Setting<Float> brightness = new Setting<>("brightness", "sped", 50f, 0.1f, 50f, 0.01f);


    @EventSubscribe
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (mc.player == null || mc.world == null || mc.gameSettings == null) return;
        mc.gameSettings.gammaSetting = brightness.getValue();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.world == null || mc.player == null) return;
        oldGamma = mc.gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.world == null || mc.player == null) return;
        mc.gameSettings.gammaSetting = oldGamma;
    }
}
