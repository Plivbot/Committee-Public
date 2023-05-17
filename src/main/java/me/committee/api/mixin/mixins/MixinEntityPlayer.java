package me.committee.api.mixin.mixins;

import me.committee.Committee;
import me.committee.impl.event.events.player.EntityCollisionEvent;
import me.committee.impl.event.events.player.PushedByWaterEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer {
    @Inject(
            method = "applyEntityCollision",
            at = @At("HEAD"),
            cancellable = true
    )
    public void applyEntityCollision(Entity p_applyEntityCollision_1_, CallbackInfo ci) {
        final EntityCollisionEvent event = new EntityCollisionEvent();
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(
            method = "isPushedByWater",
            at = @At("HEAD"),
            cancellable = true
    )
    public void isPushedByWater(CallbackInfoReturnable<Boolean> cir) {
        final PushedByWaterEvent event = new PushedByWaterEvent();
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            cir.setReturnValue(false);
    }
}
