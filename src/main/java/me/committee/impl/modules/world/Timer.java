package me.committee.impl.modules.world;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.mixin.mixins.accessors.AccessorMinecraft;
import me.committee.api.mixin.mixins.accessors.AccessorTimer;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.player.PlayerUpdateEvent;

public class Timer extends Module {

    private final Setting<Float> speed = new Setting<>("Speed", new String[]{"Spd"},"The multiplier value.", 4.2f, 0.1f, 100000f, 0.1f); // Thank you to SpicyBigDaddy for correcting the 4.6 to 4.2

    public Timer() {
        super("Timer", new String[]{"TimerSpeed"}, "Speeds up client side tick.", Category.WORLD);
    }

    @EventSubscribe
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        setTimerValue(50 / speed.getValue());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        setTimerValue(50);
    }

    private void setTimerValue(float timerValue) {
        ((AccessorTimer) ((AccessorMinecraft) mc).getTimer()).setTickLength(timerValue);
    }
}
