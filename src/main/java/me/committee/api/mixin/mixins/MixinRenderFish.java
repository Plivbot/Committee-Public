package me.committee.api.mixin.mixins;

import me.committee.Committee;
import me.committee.api.setting.Colour;
import me.committee.impl.event.events.render.FishLineColourEvent;
import me.committee.impl.event.events.render.RenderFishLineEvent;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.entity.projectile.EntityFishHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderFish.class)
public abstract class MixinRenderFish {

    @Inject(
            method = "doRender(Lnet/minecraft/entity/projectile/EntityFishHook;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;disableLighting()V",
                    shift = At.Shift.AFTER
            )
    )
    public void doRender(EntityFishHook vec3d, double d4, double d5, double d6, float d7, float f11, CallbackInfo ci) {
        final RenderFishLineEvent event = new RenderFishLineEvent();
        Committee.EVENT_BUS.post(event);
    }

    @Redirect(
            method = "doRender(Lnet/minecraft/entity/projectile/EntityFishHook;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/BufferBuilder;color(IIII)Lnet/minecraft/client/renderer/BufferBuilder;"
            )
    )
    public BufferBuilder color(BufferBuilder bufferBuilder, int red, int green, int blue, int alpha) {
        final FishLineColourEvent event = new FishLineColourEvent(new Colour(red, green, blue, alpha));
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled()) return bufferBuilder.color(event.getColour().getRed(), event.getColour().getGreen(), event.getColour().getBlue(), event.getColour().getAlpha());
        else return bufferBuilder.color(red, green, blue, alpha);
    }


}
