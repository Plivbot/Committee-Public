package me.committee.impl.modules.world;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.mixin.mixins.accessors.AccessorMinecraft;
import me.committee.api.mixin.mixins.accessors.AccessorTimer;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.player.PlayerUpdateEvent;

public class TimerSwitch extends Module {

    private final Setting<Float> speed = new Setting<>("Speed", "The multiplier value for the slow sleed.", 4.2f, 0.1f, 10f, 0.1f); // Thank you to SpicyBigDaddy for correcting the 4.6 to 4.2 (again)
    private final Setting<Float> speedFast = new Setting<>("SpeedFast", "The multiplier value for the fast sleed.", 100.0f, 0.1f, 5000f, 0.1f);
    private final Setting<Integer> tickFast = new Setting<>("TickFast", "Time to switch to faster timer.", 4, 0, 10, 1);
    private final Setting<Integer> tickSlow = new Setting<>("TickSlow", "Time to switch to slower timer.", 7, 1, 10, 1); // Should be the higher of the two numbers

    private int updates;

    public TimerSwitch() {
        super("TimerSwitch", new String[]{"SuperTimer", "FastTimer", "TimerFast"}, "Speeds up client side tick.", Category.WORLD);
    }

    @EventSubscribe
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (this.updates == tickFast.getValue())
            setTimerValue(50 / speedFast.getValue());
        else if (this.updates >= tickSlow.getValue()) {
            setTimerValue(50 / speed.getValue());
            this.updates = 0;
        }
        updates++;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.updates = 0;
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
