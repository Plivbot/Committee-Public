package me.committee.api.mixin.mixins;

import me.committee.Committee;
import me.committee.impl.event.events.render.RenderPortalEvent;
import me.committee.impl.event.events.render.RenderPotionsEvent;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame {

    @Inject(method = "renderPotionEffects", at = @At("HEAD"), cancellable = true)
    public void renderPotionEffects(ScaledResolution resolution, CallbackInfo ci) {
        final RenderPotionsEvent event = new RenderPotionsEvent();
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "renderPortal", at = @At("HEAD"), cancellable = true)
    public void renderPortal(float timeInPortal, ScaledResolution scaledRes, CallbackInfo ci) {
        final RenderPortalEvent event = new RenderPortalEvent();
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            ci.cancel();
    }

}



