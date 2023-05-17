package me.committee.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class PacketUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean eventStage = false;

    /**
     * Sends a packet and makes the mixin skip sending the event for it
     * @param packet the packet to send
     */
    public static void sendPacketNoEvent(Packet<?> packet) {
        eventStage = true;
        mc.getConnection().sendPacket(packet);
    }
}
