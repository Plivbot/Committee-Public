package me.committee.impl.event.events.network;

import me.committee.api.eventsystem.event.Event;
import net.minecraft.network.Packet;

public class ReceivePacketEvent extends Event {

    private final Packet<?> packet;

    public ReceivePacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

}
