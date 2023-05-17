package me.committee.api.mixin.mixins;

import me.committee.Committee;
import me.committee.impl.event.events.chat.ServerChatMessageEvent;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
    @ModifyArg(method = "handleChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;addChatMessage(Lnet/minecraft/util/text/ChatType;Lnet/minecraft/util/text/ITextComponent;)V"), index = 1)
    private ITextComponent balls(ITextComponent message) {
        final ServerChatMessageEvent event = new ServerChatMessageEvent(message);
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled()) {
           return event.getTextComponent();
        }
        return message;
    }
}
