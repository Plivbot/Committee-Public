package me.committee.api.util;

import me.committee.api.mixin.mixins.accessors.AccessorRenderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MathUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static double round(double num, int decimalPlaces) {
        return Double.parseDouble(String.format("%." + decimalPlaces + "f", num));
    }

    public static boolean canParseNumber(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Number clamp(Number value, Number minimum, Number maximum) {
        final float val = value.floatValue();
        return val < minimum.floatValue() ? minimum : val > maximum.floatValue() ? maximum : value;
    }

    public static Vec3d getInterpolatedPosition(Entity entity, float partialTicks) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
        return new Vec3d(x, y, z);
    }

    @SuppressWarnings("Duplicates")
    public static float[] calculateLookAt(Vec3d from, Vec3d to) {
        double dirx = from.x - to.x;
        double diry = from.y - to.y;
        double dirz = from.z - to.z;

        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

        dirx /= len;
        diry /= len;
        dirz /= len;

        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);

        pitch = pitch * 180.0d / Math.PI;
        yaw = yaw * 180.0d / Math.PI;

        yaw += 90f;

        return new float[]{(float) yaw, (float) pitch};
    }

    public static Vec3d getRenderTranslation(Entity entity, float partialTicks) {
        final double renderPosX = ((AccessorRenderManager) Minecraft.getMinecraft().getRenderManager()).getRenderPosX();
        final double renderPosY = ((AccessorRenderManager) Minecraft.getMinecraft().getRenderManager()).getRenderPosY();
        final double renderPosZ = ((AccessorRenderManager) Minecraft.getMinecraft().getRenderManager()).getRenderPosZ();
        return MathUtil.getInterpolatedPosition(entity, partialTicks)
                .subtract(renderPosX, renderPosY, renderPosZ)
                .subtract(entity.posX, entity.posY, entity.posZ);
    }

    public static Vec3d getRenderTranslation(Vec3d location) {
        final double renderPosX = ((AccessorRenderManager) Minecraft.getMinecraft().getRenderManager()).getRenderPosX();
        final double renderPosY = ((AccessorRenderManager) Minecraft.getMinecraft().getRenderManager()).getRenderPosY();
        final double renderPosZ = ((AccessorRenderManager) Minecraft.getMinecraft().getRenderManager()).getRenderPosZ();
        return location.subtract(renderPosX, renderPosY, renderPosZ);
    }

    public static double getDamage(Vec3d position, Entity entity) {
        float power = 6.0f * 2.0F;

        if (!entity.isImmuneToExplosions()) {
            double distance = entity.getDistance(position.x, position.y, position.z) / (double) power;

            if (distance <= 1.0D) {
                double relativeX = entity.posX - position.x;
                double relativeY = entity.posY + (double) entity.getEyeHeight() - position.y;
                double relativeZ = entity.posZ - position.z;
                double relative = MathHelper.sqrt(relativeX * relativeX + relativeY * relativeY + relativeZ * relativeZ);

                if (relative != 0.0D) {
                    double blockDensity = mc.world.getBlockDensity(position, entity.getEntityBoundingBox());
                    double f = (1.0D - distance) * blockDensity;

                    //entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (float)((int)((f * f + f) / 2.0D * 7.0D * (double)power + 1.0D)));

                    return (int) ((f * f + f) / 2.0D * 7.0D * (double) power + 1.0D);
                }
            }
        }

        return 0;
    }

    public static double[] directionSpeed(double speed) {
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
        final double posX = (forward * speed * cos + side * speed * sin);
        final double posZ = (forward * speed * sin - side * speed * cos);
        return new double[]{posX, posZ};
    }
}
