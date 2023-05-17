package me.committee.backend.commands.senders;

import me.committee.backend.Client;
import me.committee.packetapi.Packet;

import java.nio.charset.StandardCharsets;

public class UserCommandSender implements CommandSender {

    private final Client client;
    private int permissionLevel;

    public UserCommandSender(Client client, int permissionLevel) {
        this.client = client;
        this.permissionLevel = permissionLevel;
    }

    @Override
    public void sendMessage(String message) {
        Packet p = new Packet(Packet.CHAT, 255);
        p.setData(message.getBytes(StandardCharsets.UTF_8));
        client.queuePacket(p);
    }

    @Override
    public boolean isServer() {
        return false;
    }

    @Override
    public int getPermissionLevel() {
        return permissionLevel;
    }
}
