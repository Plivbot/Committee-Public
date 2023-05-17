package me.committee.impl.modules.combat;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.api.util.MathUtil;
import me.committee.api.util.InventoryUtil;
import me.committee.api.util.SwapUtil;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Surround extends Module {

    private final Setting<Integer> delay = new Setting<>("Delay", new String[]{"Time", "MS"}, "The time in milliseconds between placing blocks", 1, 0, 1000, 1);
    private final Setting<Boolean> centre = new Setting<>("Centre", new String[]{"Center", "Middle", "Mid"}, "Teleports you to the centre of the surround.", true);
    private final Setting<Boolean> autoDisable = new Setting<>("AutoDisable", new String[]{"Disable", "SingleUse"}, "Will automatically disable the module.", false);

    public Surround() {
        super("Surround", new String[]{"NoCrystal"}, "Automatically surrounds you in obsidian to protect you from crystal blasts.", Category.COMBAT);
    }

    @EventSubscribe
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        int firstHolding = mc.player.inventory.currentItem;


        if (mc.player == null || mc.world == null)
            return;

        final Vec3d interpPos = MathUtil.getInterpolatedPosition(mc.player, mc.getRenderPartialTicks());

        final Vec3d[] blockPositions = new Vec3d[] {
                interpPos.add(0, 0, 1),
                interpPos.add(0, 0, -1),
                interpPos.add(1, 0, 0),
                interpPos.add(-1, 0, 0),
        };

        for (Vec3d vec3d : blockPositions) {
            SwapUtil.silentSwap(mc, firstHolding, InventoryUtil.findObsidian(true), false);
            this.placeBlock(new BlockPos(vec3d));
            SwapUtil.silentSwap(mc, firstHolding, InventoryUtil.findObsidian(true), true);
        }
    }


    private void placeBlock(BlockPos blockPos) {
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockPos.add(0, -1, 0), EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
    }


}
