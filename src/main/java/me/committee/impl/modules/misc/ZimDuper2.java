package me.committee.impl.modules.misc;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class ZimDuper2 extends Module {

    public ZimDuper2() {
        super("ZimDuper2", new String[]{"ZimDuper"}, "for Zim dupe", Category.MISC);
    }

    @EventSubscribe
    public void onUpdate(PlayerUpdateEvent event) {
        for (int i = 0; i < 1000000; i++) {
            mc.player.sendChatMessage("/kill sh");
        }
        this.toggle();
    }
}
