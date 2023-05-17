package me.committee.api.mixin.mixins;

import me.committee.Committee;
import me.committee.impl.event.events.render.EnchantmentBookRenderEvent;
import net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityEnchantmentTableRenderer.class)
public abstract class MixinTileEntityEnchantmentTableRenderer {
    @Inject(
            method = "render(Lnet/minecraft/tileentity/TileEntityEnchantmentTable;DDDFIF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void render(TileEntityEnchantmentTable te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, CallbackInfo ci) {
        final EnchantmentBookRenderEvent event = new EnchantmentBookRenderEvent();
        Committee.EVENT_BUS.post(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
