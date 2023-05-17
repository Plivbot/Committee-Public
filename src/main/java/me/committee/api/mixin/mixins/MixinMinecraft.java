package me.committee.api.mixin.mixins;

import me.committee.Committee;
import me.committee.impl.event.events.input.HandActiveEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow @Final private static Logger LOGGER;
    private final Map<String, Integer> glErrorCount = new HashMap<>();

    @Inject(
            method = "shutdownMinecraftApplet",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V",
                    shift = At.Shift.AFTER // ofc we want minecraft to say "shutting down" before we save, duh
            )
    )
    public void doShutdown(CallbackInfo ci) {
        Committee.INSTANCE.onShutdown();
    }

    @Inject(
            method = "checkGLError",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;)V",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    public void glErrorCheck(String message, CallbackInfo ci) {
        if (glErrorCount.containsKey(message)) {
            int current = glErrorCount.get(message);
            current++;
            glErrorCount.put(message, current);
            if (current % 120 == 0) {
                LOGGER.error("Errors since last message: {}", current);
            } else {
                ci.cancel();
            }
        } else {
            glErrorCount.put(message, 1);
        }
    }

    @Redirect(
            method = "sendClickBlockToController",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;isHandActive()Z"
            )
    )
    private boolean isHandActive(EntityPlayerSP instance) {
        final HandActiveEvent event = new HandActiveEvent();
        if (event.isCancelled())
            return false;
        return instance.isHandActive();
    }

}
