package me.committee.api.util;

import me.committee.api.mixin.mixins.accessors.AccessorActiveRenderInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;

/**
 * From ForgeHax
 */
public class VectorUtils {

    static Matrix4f modelMatrix = new Matrix4f();
    static Matrix4f projectionMatrix = new Matrix4f();

    public static ScreenLocation toScreenPos(Vec3d vec3d) {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.getRenderViewEntity() == null)
            return new ScreenLocation(0.D, 0.D, false);

        Vec3d camPos = ActiveRenderInfo.getCameraPosition();
        Vec3d eyePos = ActiveRenderInfo.projectViewFromEntity(mc.getRenderViewEntity(), mc.getRenderPartialTicks());

        float vecX = (float) ((camPos.x + eyePos.x) - (float) vec3d.x);
        float vecY = (float) ((camPos.y + eyePos.y) - (float) vec3d.y);
        float vecZ = (float) ((camPos.z + eyePos.z) - (float) vec3d.z);

        Vector4f pos = new Vector4f(vecX, vecY, vecZ, 1.f);

        final FloatBuffer MODELVIEW = AccessorActiveRenderInfo.getMODELVIEW();
        final FloatBuffer PROJECTION = AccessorActiveRenderInfo.getPROJECTION();

        if (MODELVIEW == null || PROJECTION == null)
            return new ScreenLocation(0.D, 0.D, false);

        modelMatrix.load(MODELVIEW.asReadOnlyBuffer());
        projectionMatrix.load(PROJECTION.asReadOnlyBuffer());

        vecTransformCoordinate(pos, modelMatrix);
        vecTransformCoordinate(pos, projectionMatrix);

        if (pos.w > 0.f) {
            pos.x *= -100000;
            pos.y *= -100000;
        } else {
            float invert = 1.f / pos.w;
            pos.x *= invert;
            pos.y *= invert;
        }

        ScaledResolution res = new ScaledResolution(mc);
        float halfWidth = (float) res.getScaledWidth() / 2.0f;
        float halfHeight = (float) res.getScaledHeight() / 2.0f;

        pos.x = halfWidth + (0.5f * pos.x * res.getScaledWidth() + 0.5f);
        pos.y = halfHeight - (0.5f * pos.y * res.getScaledHeight() + 0.5f);

        return new ScreenLocation(pos.x, pos.y, !(pos.x < 0) && !(pos.y < 0) && !(pos.x > res.getScaledWidth()) && !(pos.y > res.getScaledHeight()));

    }

    private static void vecTransformCoordinate(Vector4f vec, Matrix4f matrix) {
        float x = vec.x;
        float y = vec.y;
        float z = vec.z;
        vec.x = (x * matrix.m00) + (y * matrix.m10) + (z * matrix.m20) + matrix.m30;
        vec.y = (x * matrix.m01) + (y * matrix.m11) + (z * matrix.m21) + matrix.m31;
        vec.z = (x * matrix.m02) + (y * matrix.m12) + (z * matrix.m22) + matrix.m32;
        vec.w = (x * matrix.m03) + (y * matrix.m13) + (z * matrix.m23) + matrix.m33;
    }

    public static class ScreenLocation {

        private final double x;
        private final double y;

        private final boolean show;

        public ScreenLocation(double x, double y, boolean show) {
            this.x = x;
            this.y = y;
            this.show = show;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public boolean isShown() {
            return show;
        }

    }


}
