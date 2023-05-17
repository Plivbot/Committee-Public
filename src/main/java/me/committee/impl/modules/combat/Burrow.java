package me.committee.impl.modules.combat;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.api.util.InventoryUtil;
import me.committee.api.util.SwapUtil;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class Burrow extends Module {

    private final Setting<Boolean> centre = new Setting<>("Centre", new String[]{"Center"}, "Centers you to the middle of the burrow block.", true);

    public Burrow() {
        super("Burrow", new String[]{"Blocklag"}, "Places a block where you stand.", Category.COMBAT);
    }

    private BlockPos blockPos;

    @Override
    public void onEnable() {
        super.onEnable();

        if (mc.player == null) {
            this.setEnabled(false);
            return;
        }

        if (this.centre.getValue()) {
            final double[] positions = {Math.floor(mc.player.posX) + 0.5, mc.player.posY, Math.floor(mc.player.posZ) + 0.5};
            mc.player.connection.sendPacket(new CPacketPlayer.Position(positions[0], positions[1], positions[2], true));
            mc.player.setPosition(positions[0], positions[1], positions[2]);
        }

        this.blockPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        mc.player.jump();

       // mc.player.inventory.
    }


    @EventSubscribe
    public void onUpdate(PlayerUpdateEvent event) {
        int firstHolding = mc.player.inventory.currentItem;

        if (mc.player.posY > blockPos.getY() + 1.05) {
            SwapUtil.silentSwap(mc, firstHolding, InventoryUtil.findObsidian(true), false);
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.blockPos.down(), EnumFacing.UP, EnumHand.MAIN_HAND, 0, 0, 0));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 9, mc.player.posZ, false));
            SwapUtil.silentSwap(mc, firstHolding, InventoryUtil.findObsidian(true), true);

            this.setEnabled(false);
        }
    }
}
