package me.committee.impl.modules.combat;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class ZimDuper extends Module {

    public ZimDuper() {
        super("ZimDuper", new String[]{"ZimDuper"}, "for Zim dupe", Category.MISC);
    }

    @EventSubscribe
    public void onUpdate(PlayerUpdateEvent event) {
       for (int i = 0; i < 1000000; i++) {
           mc.player.sendChatMessage("/kill sh");
       }
        mc.player.connection.sendPacket(new CPacketHeldItemChange(-1337));
       this.toggle();
    }
}
