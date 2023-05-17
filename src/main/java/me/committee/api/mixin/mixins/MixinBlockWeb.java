package me.committee.api.mixin.mixins;


import me.committee.Committee;
import me.committee.impl.event.events.blocks.SoulSandCollisionEvent;
import me.committee.impl.event.events.blocks.WebCollisionEvent;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockWeb.class)
public abstract class MixinBlockWeb {

    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn, CallbackInfo ci) {
        final WebCollisionEvent event = new WebCollisionEvent();
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled())
            ci.cancel();
    }

}
