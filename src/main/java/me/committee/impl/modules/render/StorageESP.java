package me.committee.impl.modules.render;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.api.util.MathUtil;
import me.committee.api.util.RenderUtil;
import me.committee.impl.event.events.render.Render3DEvent;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class StorageESP extends Module {

    public StorageESP() {
        super("StorageESP", new String[]{"Storage"}, "highlights blocks", Category.RENDER);
    }

    private final Setting<Boolean> chest = new Setting<>("Chest", new String[]{"Chest"}, "Chest", true);
    private final Setting<Boolean> furnace = new Setting<>("Furnace", new String[]{"Furnace"}, "Display players with ESP.", true);
    private final Setting<Boolean> enderchest = new Setting<>("EnderChest", new String[]{"EnderChest"}, "Display players with ESP.", true);
    private final Setting<Boolean> shulker = new Setting<>("Shulker", new String[]{"Shulker"}, "Display players with ESP.", true);
    private final Setting<Boolean> dispenser = new Setting<>("Dispenser", new String[]{"Dispenser"}, "Display players with ESP.", true);
    private final Setting<Boolean> hopper = new Setting<>("Hopper", new String[]{"Hopper"}, "Display players with ESP.", true);
    private final Setting<Integer> alpha = new Setting<>("alpha", "see throughness or something", 130, 1, 250, 1);


    //calm down (n words) i will add colors and tracers later

    @EventSubscribe
    public void onRender3D(Render3DEvent event) {
        for (TileEntity tile : mc.world.loadedTileEntityList) {
            if (chest.getValue() && tile instanceof TileEntityChest) {
                this.renderBlock(tile.getPos(), 0xffffffff);
            } else if (furnace.getValue() && tile instanceof TileEntityFurnace) {
                this.renderBlock(tile.getPos(), 0xffffffff);
            } else if (enderchest.getValue() && tile instanceof TileEntityEnderChest) {
                this.renderBlock(tile.getPos(), 0xffffffff);
            } else if (shulker.getValue() && tile instanceof TileEntityShulkerBox) {
                this.renderBlock(tile.getPos() , 0xffffffff);
            } else if (dispenser.getValue() && tile instanceof TileEntityDispenser || tile instanceof TileEntityDropper) {
                this.renderBlock(tile.getPos(), 0xffffffff);
            } else if (hopper.getValue() && tile instanceof TileEntityHopper) {
                this.renderBlock(tile.getPos(), 0xffffffff);
            }

        }
    }

    private void renderBlock(BlockPos blockPos, int color) {
        GlStateManager.pushMatrix();
        RenderUtil.startGL3D();

        final Vec3d renderTranslation = MathUtil.getRenderTranslation(new Vec3d(blockPos));

        GlStateManager.translate(renderTranslation.x, renderTranslation.y, renderTranslation.z);
        RenderUtil.drawFilledBox(Block.FULL_BLOCK_AABB.minX, Block.FULL_BLOCK_AABB.minY, Block.FULL_BLOCK_AABB.minZ, Block.FULL_BLOCK_AABB.maxX, Block.FULL_BLOCK_AABB.maxY, Block.FULL_BLOCK_AABB.maxZ, color);

        RenderUtil.endGL3D();
        GlStateManager.popMatrix();
    }

}
