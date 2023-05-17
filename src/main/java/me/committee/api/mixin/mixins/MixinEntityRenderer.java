package me.committee.api.mixin.mixins;

import me.committee.Committee;
import me.committee.api.eventsystem.event.Event;
import me.committee.impl.event.events.render.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

    @Shadow
    @Final
    private Minecraft mc;

    @Inject(method = "drawNameplate", at = @At("HEAD"), cancellable = true)
    private static void drawNameplate(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking, CallbackInfo ci) {
        final RenderLivingLabelEvent event = new RenderLivingLabelEvent();
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;renderGameOverlay(F)V", shift = At.Shift.AFTER))
    public void updateCameraAndRender(float partialTicks, long nanoTime, CallbackInfo ci) {
        final Render2DEvent event = new Render2DEvent(partialTicks);
        Committee.EVENT_BUS.post(event);
    }

    @Inject(
            method = "renderWorldPass",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand(FI)V",
                    shift = At.Shift.BEFORE
            )
    )
    public void renderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        mc.profiler.endStartSection("committee");
        final Render3DEvent event = new Render3DEvent(partialTicks);
        Committee.EVENT_BUS.post(event);
    }

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    private void hurtCameraEffect(float partialTicks, CallbackInfo ci) {
        RenderHurtCamEvent event = new RenderHurtCamEvent();
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            ci.cancel();

    }

    @Inject(method = "getMouseOver", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"), cancellable = true)
    public void getMouseOver(float partialTicks, CallbackInfo ci) {
        RenderEntityOverMouseEvent event = new RenderEntityOverMouseEvent();
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            ci.cancel();
    }


    @Redirect(method = "orientCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;rayTraceBlocks(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/RayTraceResult;"))
    public RayTraceResult raytrace(WorldClient instance, Vec3d vec3d, Vec3d vec3dd) {
        BlockCameraEvent event = new BlockCameraEvent();
        Committee.EVENT_BUS.post(event);

        if (event.isCancelled()) {
            return null;
        } else {
            return instance.rayTraceBlocks(vec3d, vec3dd);
        }
    }

    @Redirect(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getMaterial()Lnet/minecraft/block/material/Material;", ordinal = 0))
    public Material water(IBlockState instance) {
        RenderWaterFogEvent event = new RenderWaterFogEvent();
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            return Material.AIR;
        } else {
            return instance.getMaterial();
        }
    }

    @ModifyArg(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;setFogDensity(F)V", ordinal = 5))
    public float lava(float param) {
        RenderLavaFogEvent event = new RenderLavaFogEvent(param);
        Committee.EVENT_BUS.post(event);
        return event.getDensity();
    }

}
