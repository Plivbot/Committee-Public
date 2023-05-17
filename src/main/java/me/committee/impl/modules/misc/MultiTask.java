package me.committee.impl.modules.misc;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.input.HandActiveEvent;
import me.committee.impl.event.events.player.PlayerDamageBlockEvent;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import net.minecraft.network.play.client.CPacketHeldItemChange;


public class MultiTask extends Module {

    public MultiTask() {super("MultiTask", new String[]{"MultiUse"}, "Allows you to use main-hand and off-hand at the same time.", Category.MISC);}

    @EventSubscribe
    public void onHandActive(HandActiveEvent event) {
        event.cancel();
    }

}
