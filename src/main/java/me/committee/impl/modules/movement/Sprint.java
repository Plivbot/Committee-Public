package me.committee.impl.modules.movement;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.input.MoveEvent;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import me.committee.impl.event.events.player.PlayerWalkingUpdateEvent;

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", new String[]{"sprint"}, "Auto sprints.", Category.MOVEMENT);
    }

    @EventSubscribe
    public void onPlayerWalkingUpdate(PlayerWalkingUpdateEvent event) {
        if (mc.player.moveForward != 0 || mc.player.moveStrafing != 0) {
            mc.player.setSprinting(true);
        }
    }
}
