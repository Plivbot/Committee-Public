package me.committee.api.mixin.mixins;

import me.committee.Committee;
import me.committee.impl.event.events.render.FireOverlayEvent;
import me.committee.impl.event.events.render.SuffocationOverlayEvent;
import me.committee.impl.event.events.render.WaterOverlayEvent;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    @Inject(method = "renderSuffocationOverlay", at = @At(value = "HEAD"), cancellable = true )
    public void renderSuffocationOverlay(TextureAtlasSprite sprite, CallbackInfo ci) {
        final SuffocationOverlayEvent event = new SuffocationOverlayEvent();
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "renderWaterOverlayTexture", at = @At(value = "HEAD"), cancellable = true )
    public void renderWaterOverlayTexture(float partialTicks, CallbackInfo ci){
        final WaterOverlayEvent event = new WaterOverlayEvent();
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "renderFireInFirstPerson", at = @At(value = "HEAD"), cancellable = true )
    public void renderFireInFirstPerson(CallbackInfo ci){
        final FireOverlayEvent event = new FireOverlayEvent();
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            ci.cancel();
    }




}
