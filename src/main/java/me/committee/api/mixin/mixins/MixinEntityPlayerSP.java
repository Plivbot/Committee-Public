package me.committee.api.mixin.mixins;


import com.mojang.authlib.GameProfile;
import me.committee.Committee;
import me.committee.impl.event.events.input.MoveEvent;
import me.committee.impl.event.events.player.*;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

    private MoveEvent moveEvent;

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(
            method = "move",
            at = @At(
                    "HEAD"
            )
    )
    public void move(MoverType type, double x, double y, double z, CallbackInfo ci) {
        this.moveEvent = new MoveEvent(x, y, z);
        Committee.EVENT_BUS.post(this.moveEvent);
    }


    @ModifyArg(
            method = "move",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"
            ),
            index = 1
    )
    public double x(double x) {
        return this.moveEvent.isCancelled() ? this.moveEvent.getX() : x;
    }

    @ModifyArg(
            method = "move",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"
            ),
            index = 2
    )
    public double y(double y) {
        return this.moveEvent.isCancelled() ? this.moveEvent.getY() : y;
    }

    @ModifyArg(
            method = "move",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"
            ),
            index = 3
    )
    public double z(double z) {
        return this.moveEvent.isCancelled() ? this.moveEvent.getZ() : z;
    }

    @Inject(method = "onUpdate", at = @At("HEAD"))
    public void onUpdate(CallbackInfo ci) {
        final PlayerUpdateEvent event = new PlayerUpdateEvent();
        Committee.EVENT_BUS.post(event);
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void onUpdateWalkingPlayer(CallbackInfo ci) {
        final PlayerWalkingUpdateEvent event = new PlayerWalkingUpdateEvent();
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Redirect(
            method = "onLivingUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;isHandActive()Z",
                    ordinal = 0
            )
    )
    public boolean isHandActive(EntityPlayerSP instance) {
        final HandActiveSlowdownEvent event = new HandActiveSlowdownEvent();
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            return false;
        return instance.isHandActive();
    }

    @Inject(
            method = "pushOutOfBlocks",
            at = @At("HEAD"),
            cancellable = true
    )
    public void pushOutOfBlocks(double d2, double f, double blockpos, CallbackInfoReturnable<Boolean> cir) {
        final PushOutOfBlocksEvent event = new PushOutOfBlocksEvent();
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            cir.setReturnValue(false);
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At("HEAD"),
            cancellable = true
    )
    public void inPortal(CallbackInfo ci) {
        final InPortalEvent event = new InPortalEvent();
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            ci.cancel();
    }
}
