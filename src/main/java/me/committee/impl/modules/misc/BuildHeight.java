package me.committee.impl.modules.misc;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.mixin.mixins.accessors.AccessorCPacketPlayerTryUseItemOnBlock;
import me.committee.api.module.Module;
import me.committee.impl.event.events.network.SendPacketEvent;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;

public class BuildHeight extends Module {

    public BuildHeight() {super ("BuildHeight", new String[]{"BouwHoogtes"}, "you can place blocks on obi roof", Category.MISC);}

    @EventSubscribe
    public void onSendPacket(SendPacketEvent event) {
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            final CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock) event.getPacket();
            if (packet.getPos().getY() == 255 && packet.getDirection() == EnumFacing.UP) {
                ((AccessorCPacketPlayerTryUseItemOnBlock) packet).setPlacedBlockDirection(EnumFacing.DOWN);
            }
        }
    }
}
// I MADE THIS NOT STUPID CODER TBM HES DUMB AND STINKY I'M GOING TO KILL HIM.