package me.committee.impl.modules.movement;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class Step extends Module {

    private final Setting<Step.Mode> mode = new Setting<>("Mode", new String[]{"M"}, "The mode to use.", Step.Mode.ONE);


    public Step() {super("Step", new String[]{"Stap"}, "Lets you walk up multiple blocks", Category.MOVEMENT);}


    private double[] direction() {
        float forward = mc.player.movementInput.moveForward;
        float side = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();

        if (forward != 0) {
            if (side > 0) {
                yaw += (forward > 0 ? -45 : 45);
            } else if (side < 0) {
                yaw += (forward > 0 ? 45 : -45);
            }
            side = 0;

            //forward = clamp(forward, 0, 1);
            if (forward > 0) {
                forward = 1;
            } else if (forward < 0) {
                forward = -1;
            }
        }

        final double sin = Math.sin(Math.toRadians(yaw + 90));
        final double cos = Math.cos(Math.toRadians(yaw + 90));
        final double posX = (forward * cos + side * sin);
        final double posZ = (forward * sin - side * cos);
        return new double[]{posX, posZ};
    }

    @EventSubscribe
    public void onPlayerUpdate(PlayerUpdateEvent event) {

        final EntityPlayerSP mp = mc.player;
        final WorldClient mw = mc.world;
        final AxisAlignedBB b = mc.player.getEntityBoundingBox();
        final double x = mc.player.posX;
        final double y = mc.player.posY;
        final double z = mc.player.posZ;
        final boolean G = mc.player.onGround;
        final double[] blockDir = this.direction();
        final double xx = mc.player.posX + blockDir[0];
        final double zz = mc.player.posZ + blockDir[1];
        final Block block0 = mw.getBlockState(new BlockPos(xx, b.maxY, zz)).getBlock();
        final Block block = mw.getBlockState(new BlockPos(xx, b.maxY + 1, zz)).getBlock();
        final Block block2 = mw.getBlockState(new BlockPos(xx, b.maxY + 2, zz)).getBlock();
        final Block block3 = mw.getBlockState(new BlockPos(x, b.maxY + 1, z)).getBlock();
        final Block block4 = mw.getBlockState(new BlockPos(x, b.maxY + 2, z)).getBlock();
        float[] one = {0.42f, 0.75f};
        float[] two = {0.4f, 0.75f, 0.5f, 0.41f, 0.83f, 1.16f, 1.41f ,1.57f, 1.58f, 1.42f};

        if (!(block instanceof BlockAir && block2 instanceof BlockAir && block3 instanceof BlockAir && block4 instanceof BlockAir))
            return;
        if (mp.collidedHorizontally && mode.getValue() == Mode.ONE && G && mc.player.fallDistance == 0 && !mc.gameSettings.keyBindJump.isPressed() && mc.player.collidedVertically) {
            if (!(block0 instanceof BlockAir))
                return;
            for (float i : one) {
            mp.connection.sendPacket(
                    new CPacketPlayer.Position(x, y + i, z, true
                    ));
                mp.setPosition(x, y + i, z);
            }
        }
        if (mp.collidedHorizontally && mode.getValue() == Mode.TWO && G && mp.fallDistance == 0 && !mc.gameSettings.keyBindJump.isPressed() && mc.player.collidedVertically) {
            if (block0 instanceof BlockAir) {
                for (float i : two) {
                    mp.connection.sendPacket(
                            new CPacketPlayer.Position(x, y + i, z, true
                            ));
                    mp.setPosition(x, y + i, z);
                }
            } else {
                for (float i : two) {
                    mp.connection.sendPacket(
                            new CPacketPlayer.Position(x, y + i, z, true
                            ));
                    mp.setPosition(x, y + i, z);
                }
            }
        }
    }

    private enum Mode {
        ONE, TWO
    }
}
