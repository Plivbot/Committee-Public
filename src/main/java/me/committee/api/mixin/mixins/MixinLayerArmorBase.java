package me.committee.api.mixin.mixins;

import me.committee.Committee;
import me.committee.impl.event.events.render.RenderLayerArmourEvent;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public abstract class MixinLayerArmorBase {

    @Inject(method = "doRenderLayer", at = @At("HEAD"), cancellable = true)
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci) {
        final RenderLayerArmourEvent event = new RenderLayerArmourEvent(entitylivingbaseIn);
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            ci.cancel();
    }

}
