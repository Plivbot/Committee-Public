package me.committee.api.mixin.mixins.accessors;

import net.minecraft.network.play.client.CPacketChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketChatMessage.class)
public interface AccessorCPacketChatMessage {

    @Accessor(value = "message")
    void setMessage(String s);
}
