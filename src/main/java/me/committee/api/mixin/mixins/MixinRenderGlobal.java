package me.committee.api.mixin.mixins;

import me.committee.Committee;
import me.committee.impl.event.events.render.OutlineActiveEvent;
import me.committee.impl.event.events.render.SpawnParticleEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal {

    @Inject(method = "isOutlineActive", at = @At("HEAD"), cancellable = true)
    public void isOutlineActive(Entity entityIn, Entity viewer, ICamera camera, CallbackInfoReturnable<Boolean> cir) {
        final OutlineActiveEvent event = new OutlineActiveEvent(entityIn);
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            cir.setReturnValue(true);
    }


    @Redirect(
            method = "spawnParticle0(IZZDDDDDD[I)Lnet/minecraft/client/particle/Particle;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;getRenderViewEntity()Lnet/minecraft/entity/Entity;"
            )
    )
    public Entity spawnParticle0(Minecraft instance, int particleID, boolean ignoreRange, boolean minParticles, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) {
        final SpawnParticleEvent event = new SpawnParticleEvent(particleID);
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            return null;
        return instance.getRenderViewEntity();
    }


}
