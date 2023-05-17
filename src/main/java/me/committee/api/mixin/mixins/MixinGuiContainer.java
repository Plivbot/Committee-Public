package me.committee.api.mixin.mixins;

import me.committee.Committee;
import me.committee.impl.event.events.render.RenderTooltipEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer {

    @Shadow
    private Slot hoveredSlot;

    @Inject(method = "renderHoveredToolTip", at = @At(
            value = "HEAD",
            target = "Lnet/minecraft/client/gui/inventory/GuiContainer;renderToolTip(Lnet/minecraft/item/ItemStack;II)V"
    ),cancellable = true)
    public void renderTooltip(int p_191948_1_, int p_191948_2_, CallbackInfo ci) {
        if (this.hoveredSlot != null) {
            ItemStack itemStack = this.hoveredSlot.getStack();

            final RenderTooltipEvent event = new RenderTooltipEvent(itemStack, p_191948_1_, p_191948_2_);
            Committee.EVENT_BUS.post(event);
            if (event.isCancelled())
                ci.cancel();
        }
    }
}
