package me.committee.api.mixin.mixins;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import me.committee.Committee;
import me.committee.api.util.PacketUtil;
import me.committee.impl.event.events.network.ReceivePacketEvent;
import me.committee.impl.event.events.network.SendPacketEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager {

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        final ReceivePacketEvent event = new ReceivePacketEvent(packet);
        Committee.EVENT_BUS.post(event);
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(method = "dispatchPacket", at = @At("HEAD"), cancellable = true)
    public void dispatchPacket(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>>[] futureListeners, CallbackInfo ci) {

        final SendPacketEvent event = new SendPacketEvent(packet);
        if (PacketUtil.eventStage) {
            PacketUtil.eventStage = false;
            return;
        }

        Committee.EVENT_BUS.post(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

}
