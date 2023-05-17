package me.committee.impl.modules.world;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

// code idea stolen from cats "written" by spicy

public class Nuker extends Module {

    private final Setting<Mode> mode = new Setting<>("Mode", new String[]{"M", "HowWannaBreakBlocksUCunt"}, "The mode to use.", Mode.NORMAL);
    private final Setting<Integer> range = new Setting<>("Range", new String[]{"Distance", "Dist", "BlockBreakPlaceDistance"}, "The range to break blocks", 4, 0, 10, 1); // Sets up the range setting
    private final Setting<Boolean> crouchDown = new Setting<>("Down", new String[]{"DigDown", "CrouchDown", "MileyCyrusOnThisBitch"}, "When you crouch it will allow you to dig down.", false);

    private final Block[] disallowedBlocks = new Block[]{Blocks.BEDROCK, Blocks.END_PORTAL_FRAME, Blocks.WATER, Blocks.FLOWING_WATER, Blocks.LAVA, Blocks.FLOWING_LAVA, Blocks.PORTAL, Blocks.END_PORTAL_FRAME, Blocks.END_GATEWAY, Blocks.AIR};

    public Nuker() {
        super("Nuker", new String[]{"BlockFucker", "BlockPwner", "FuckYouAndYourBlocksRetard", "FuckingInTheKitchen"}, "Destroys blocks in a range.", Category.WORLD); // Sets up the module
    }

    @EventSubscribe
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (mc.player == null)
            return;

        if (this.mode.getValue() == Mode.NORMAL) {
            for (int x = -range.getValue(); x <= range.getValue(); x++) {
                for (int y = this.crouchDown.getValue() && mc.player.isSneaking() ? -range.getValue() : 0; y <= range.getValue(); y++) {
                    for (int z = -range.getValue(); z <= range.getValue(); z++) {
                        final BlockPos blockPos = new BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);
                        final Block block = mc.world.getBlockState(blockPos).getBlock();

                        if (this.validBlock(block)) destroyBlock(blockPos);
                    }
                }
            }
        } else {
            final EnumFacing enumfacing = mc.player.getHorizontalFacing();

            for (int i = 0; i <= this.range.getValue(); i++) {
                BlockPos bpOne = new BlockPos(i, 0, 0);
                BlockPos bpTwo = new BlockPos(i, 1, 0);

                switch (enumfacing) {
                    case NORTH: // -z
                        bpOne = new BlockPos(0, 0, -i);
                        bpTwo = new BlockPos(0, 1, -i);
                        break;
                    case SOUTH: // +z
                        bpOne = new BlockPos(0, 0, i);
                        bpTwo = new BlockPos(0, 1, i);
                        break;
                    case WEST: // -x
                        bpOne = new BlockPos(-i, 0, 0);
                        bpTwo = new BlockPos(-i, 1, 0);
                        break;
                    case EAST: // +x
                        bpOne = new BlockPos(i, 0, 0);
                        bpTwo = new BlockPos(i, 1, 0);
                        break;
                }

                final BlockPos blockPosOne = new BlockPos(mc.player.posX + bpOne.getX(), mc.player.posY + bpOne.getY(), mc.player.posZ + bpOne.getZ());
                final BlockPos blockPosTwo = new BlockPos(mc.player.posX + bpTwo.getX(), mc.player.posY + bpTwo.getY(), mc.player.posZ + bpTwo.getZ());
                final Block blockOne = mc.world.getBlockState(blockPosOne).getBlock();
                final Block blockTwo = mc.world.getBlockState(blockPosTwo).getBlock();

                if (this.validBlock(blockOne)) this.destroyBlock(blockPosOne);
                if (this.validBlock(blockTwo)) this.destroyBlock(blockPosTwo);

            }
        }
    }

    private void destroyBlock(BlockPos blockPos) {
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.UP));
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.UP));
        mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
    }

    private boolean validBlock(Block block) {
        for (Block disallowedBlock : this.disallowedBlocks) {
            if (disallowedBlock == block)
                return false;
        }
        return true;
    }

    private enum Mode {
        NORMAL, TUNNEL
    }

}



