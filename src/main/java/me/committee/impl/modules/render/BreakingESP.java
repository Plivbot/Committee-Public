package me.committee.impl.modules.render;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.util.MathUtil;
import me.committee.api.util.MessageSendHelper;
import me.committee.api.util.RenderUtil;
import me.committee.impl.event.events.network.ReceivePacketEvent;
import me.committee.impl.event.events.render.Render3DEvent;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;

public class BreakingESP extends Module {

    public BreakingESP() {
        super("BreakingESP", new String[]{""}, "highlights shit being broken", Category.RENDER);
    }

    HashMap<Integer, BlockPos> posAndProgress = new HashMap<>();

    @EventSubscribe
    public void onReceivePacket(ReceivePacketEvent event) {
        if (event.getPacket() instanceof SPacketBlockBreakAnim) {
            SPacketBlockBreakAnim packet = (SPacketBlockBreakAnim) event.getPacket();
            if (packet.getBreakerId() != mc.player.getEntityId())
                posAndProgress.put(packet.getProgress(), packet.getPosition());
        }
    }

    @EventSubscribe
    public void onRender3D(Render3DEvent event) {
        for (Map.Entry<Integer, BlockPos> entry : posAndProgress.entrySet()) {
            int progress = entry.getKey();
            BlockPos pos = entry.getValue();
            if (mc.world.getBlockState(pos).getBlock() != Blocks.AIR) {
                if (progress > 0 && progress < 4)
                    this.renderBlock(pos, 0, 255, 0);
                else if (progress > 3 && progress < 8)
                    this.renderBlock(pos, 255, 255, 0);
                else if (progress > 8)
                    this.renderBlock(pos, 255, 0, 0);
            }
        }
    }

    private void renderBlock(BlockPos blockPos, int r, int g, int b) {
        GlStateManager.pushMatrix();
        RenderUtil.startGL3D();

        final Vec3d renderTranslation = MathUtil.getRenderTranslation(new Vec3d(blockPos));

        GlStateManager.translate(renderTranslation.x, renderTranslation.y, renderTranslation.z);
        RenderUtil.drawFilledBox(Block.FULL_BLOCK_AABB.minX, Block.FULL_BLOCK_AABB.minY, Block.FULL_BLOCK_AABB.minZ, Block.FULL_BLOCK_AABB.maxX, Block.FULL_BLOCK_AABB.maxY, Block.FULL_BLOCK_AABB.maxZ, r, g, b, 125);

        RenderUtil.endGL3D();
        GlStateManager.popMatrix();
    }
}
