package me.committee.api.mixin.mixins;

import me.committee.Committee;
import me.committee.impl.event.events.render.TeamColourEvent;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Render.class)
public abstract class MixinRender {

    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    public void getTeamColour(Entity entity, CallbackInfoReturnable<Integer> cir) {
        final TeamColourEvent event = new TeamColourEvent(entity, 16777215);
        Committee.EVENT_BUS.post(event);
        cir.setReturnValue(event.getColour());
    }

}
