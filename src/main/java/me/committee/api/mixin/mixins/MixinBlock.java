package me.committee.api.mixin.mixins;

import me.committee.Committee;
import me.committee.impl.event.events.blocks.AddCollisionBoxEvent;
import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ConcurrentModificationException;
import java.util.List;

@Mixin(Block.class)
public abstract class MixinBlock {

    @Inject(
            method = "addCollisionBoxToList(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/util/math/AxisAlignedBB;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void addCollisionBoxToList(BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, AxisAlignedBB blockBox, CallbackInfo ci) {
        final AddCollisionBoxEvent event = new AddCollisionBoxEvent(pos);

        try {
            Committee.EVENT_BUS.post(event);
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
            return;
        }

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

}
