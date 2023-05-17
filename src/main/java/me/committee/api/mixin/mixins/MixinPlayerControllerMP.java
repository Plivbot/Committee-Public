package me.committee.api.mixin.mixins;

import me.committee.Committee;
import me.committee.impl.event.events.input.HandActiveEvent;
import me.committee.impl.event.events.player.PlayerDamageBlockEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP {

    @Shadow private boolean isHittingBlock;

    @Inject(
            method = "onPlayerDamageBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onPlayerDamageBlock(BlockPos blockPos, EnumFacing enumFacing, CallbackInfoReturnable<Boolean> cir) {
        final PlayerDamageBlockEvent event = new PlayerDamageBlockEvent(blockPos, enumFacing);
        Committee.EVENT_BUS.post(event);

        if (event.isCancelled())
            cir.setReturnValue(false);
    }

    @Inject(method = "getIsHittingBlock", at=@At("HEAD"), cancellable = true)
    private void getIsHittingBlock(CallbackInfoReturnable<Boolean> cir) {
        final HandActiveEvent event = new HandActiveEvent();
        if (event.isCancelled())
            cir.setReturnValue(false);
        cir.setReturnValue(this.isHittingBlock);
    }

}
