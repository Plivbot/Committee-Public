package me.committee.api.util;

import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExplosionUtil {

    private final Minecraft mc = Minecraft.getMinecraft();

    public double calculateDamage(Vec3d pos1, Entity target) {
        float size = 6;

        Set<BlockPos> set = Sets.newHashSet();
        final List<BlockPos> affectedBlockPositions = new ArrayList<>();

        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = (float) j / 15.0F * 2.0F - 1.0F;
                        double d1 = (float) k / 15.0F * 2.0F - 1.0F;
                        double d2 = (float) l / 15.0F * 2.0F - 1.0F;
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 = d0 / d3;
                        d1 = d1 / d3;
                        d2 = d2 / d3;
                        float f = size * (0.7F + mc.world.rand.nextFloat() * 0.6F);
                        double d4 = pos1.x;
                        double d6 = pos1.y;
                        double d8 = pos1.z;

                        for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            BlockPos blockpos = new BlockPos(d4, d6, d8);
                            IBlockState iblockstate = mc.world.getBlockState(blockpos);

                            /*
                            if (iblockstate.getMaterial() != Material.AIR) {
                                float f2 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.world, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(world, blockpos, (Entity) null, this);
                                f -= (f2 + 0.3F) * 0.3F;
                            }

                            if (f > 0.0F && (this.exploder == null || this.exploder.canExplosionDestroyBlock(this, this.world, blockpos, iblockstate, f))) {
                                set.add(blockpos);
                            }
                             */

                            d4 += d0 * 0.30000001192092896D;
                            d6 += d1 * 0.30000001192092896D;
                            d8 += d2 * 0.30000001192092896D;
                        }
                    }
                }
            }
        }

        affectedBlockPositions.addAll(set);
        float f3 = size * 2.0F;
        int k1 = MathHelper.floor(pos1.x - (double) f3 - 1.0D);
        int l1 = MathHelper.floor(pos1.x + (double) f3 + 1.0D);
        int i2 = MathHelper.floor(pos1.y - (double) f3 - 1.0D);
        int i1 = MathHelper.floor(pos1.y + (double) f3 + 1.0D);
        int j2 = MathHelper.floor(pos1.z - (double) f3 - 1.0D);
        int j1 = MathHelper.floor(pos1.z + (double) f3 + 1.0D);
        List<Entity> list = mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(k1, i2, j2, l1, i1, j1));
        //net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(mc.world, this, list, f3);

        for (int k2 = 0; k2 < list.size(); ++k2) {
            Entity entity = list.get(k2);

            if (!entity.isImmuneToExplosions()) {
                double d12 = entity.getDistance(pos1.x, pos1.y, pos1.z) / (double) f3;

                if (d12 <= 1.0D) {
                    double d5 = entity.posX - pos1.x;
                    double d7 = entity.posY + (double) entity.getEyeHeight() - pos1.y;
                    double d9 = entity.posZ - pos1.z;
                    double d13 = MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);

                    if (d13 != 0.0D) {
                        d5 = d5 / d13;
                        d7 = d7 / d13;
                        d9 = d9 / d13;
                        double d14 = mc.world.getBlockDensity(pos1, entity.getEntityBoundingBox());
                        double d10 = (1.0D - d12) * d14;
                        return (float) ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) f3 + 1.0D));
                    }
                }
            }
        }
        return 0;
    }
}
