package me.committee.impl.modules.world;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.api.util.BlockUtil;
import me.committee.api.util.InventoryUtil;
import me.committee.api.util.SwapUtil;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Keyboard;

public class Scaffold extends Module {

    private final Setting<Boolean> SilentSwap = new Setting<>("SilentSwap", new String[]{"SS"}, "Swaps to the block without showing it to you", false);


    public Scaffold() {
        super("Scaffold", new String[]{"BlockFly", "Tower"}, "it scaffolds", Category.WORLD);
    }

    private int firstHolding = 0;
    private int blocksPlaced;

    @Override
    public void onEnable() {
        super.onEnable();
//        this.firstHolding = 0;
        this.blocksPlaced = 0;

//        this.firstHolding = mc.player.inventory.currentItem;

    }

    @Override
    public void onDisable() {
        super.onDisable();
/*        mc.player.inventory.currentItem = firstHolding;
        mc.playerController.updateController();*/
    }

    public void place(BlockPos pos) {

        final EnumFacing availableBlock = BlockUtil.findAvailableSide(pos); //calcside looks for a block that borders the position we're placing so it can place on it
        final BlockPos blockToPlaceOn = pos.offset(availableBlock);
        final Block block = mc.world.getBlockState(blockToPlaceOn).getBlock();
        final EnumFacing availableSide = availableBlock.getOpposite();
        final boolean rightclickableBlock = block.onBlockActivated(mc.world, blockToPlaceOn, mc.world.getBlockState(blockToPlaceOn), mc.player, EnumHand.MAIN_HAND, EnumFacing.UP, 0, 0, 0);

        if (rightclickableBlock)
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));

        mc.playerController.processRightClickBlock(mc.player, mc.world, blockToPlaceOn, availableSide, new Vec3d(blockToPlaceOn.getX(), blockToPlaceOn.getY(), blockToPlaceOn.getZ()), EnumHand.MAIN_HAND);

        if (rightclickableBlock)
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));

    }


    @EventSubscribe
    public void onUpdate(PlayerUpdateEvent event) {
        firstHolding = mc.player.inventory.currentItem;


        //SwapUtil.swapToSlot(InventoryUtil.findAnyBlock());

        boolean isNotMoving = mc.player.movementInput.moveForward == 0 && mc.player.movementInput.moveStrafe == 0;

        BlockPos toPlace;

        BlockPos belowPlayer = new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ);

        toPlace = belowPlayer;

        if (BlockUtil.findAvailableSide(belowPlayer) == null) {
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                EnumFacing availableSideForBelowPlayer = BlockUtil.findAvailableSide(belowPlayer.offset(facing));
                if (availableSideForBelowPlayer != null) {
                    toPlace = belowPlayer.offset(availableSideForBelowPlayer);
                }
            }
        }

        if (!Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
            SwapUtil.silentSwap(mc, firstHolding, InventoryUtil.findAnyBlock(true), false);
            this.place(toPlace);
            SwapUtil.silentSwap(mc, firstHolding, InventoryUtil.findAnyBlock(true), true);
        }

        if (mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown() && !mc.player.onGround && (mc.player.posY - Math.floor(mc.player.posY)) <= 0.1 && isNotMoving) {
            blocksPlaced++;
            mc.player.motionY = 0.42;

            if (blocksPlaced >= 10) {
                mc.player.motionY = -0.2;
                blocksPlaced = 0;
            }
        }

        if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);

            SwapUtil.silentSwap(mc, firstHolding, InventoryUtil.findAnyBlock(true), false);
            this.place(new BlockPos(mc.player.posX, mc.player.posY - 2, mc.player.posZ));
            SwapUtil.silentSwap(mc, firstHolding, InventoryUtil.findAnyBlock(true), true);

        }
    }

}
