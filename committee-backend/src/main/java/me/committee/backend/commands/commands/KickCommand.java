package me.committee.backend.commands.commands;

import me.committee.backend.Client;
import me.committee.backend.Server;
import me.committee.backend.commands.Command;
import me.committee.backend.commands.senders.CommandSender;
import me.committee.packetapi.Packet;

import java.nio.charset.StandardCharsets;

public class KickCommand extends Command {

    public KickCommand() {
        super("kick");
    }

    @Override
    public void onExec(String[] args, CommandSender sender) {

        if (args.length < 3) {
            sender.sendMessage(
                    "example of valid usage: \"kick ip 1.2.3.4\", \"kick name Hausemaster\"");
            return;
        }

        String reason = "Kicked for unknown reason.";

        StringBuilder reasonBuilder = new StringBuilder();

        if (args.length > 3) {
            for (int i = 3; i < args.length - 1; i++) {
                reasonBuilder.append(args[i]).append(" ");
            }
            reasonBuilder.replace(reasonBuilder.length(), reasonBuilder.length(), "");
            reason = reasonBuilder.toString();
        }

        if (args[1].equalsIgnoreCase("ip")) {
            String ip = args[2];

            for (Client connection : Server.connections) {
                if (connection.getSocket().getInetAddress().getHostAddress().equalsIgnoreCase(ip)) {
                    Packet p = new Packet(Packet.KICK, 100);
                    p.setData(reason.getBytes(StandardCharsets.UTF_8));
                    connection.queuePacket(p);
                    break;
                }
            }
        } else if (args[1].equalsIgnoreCase("name")) {
            String name = args[2];

            for (Client connection : Server.connections) {
                if (connection.name.equalsIgnoreCase(name)) {
                    Packet p = new Packet(Packet.KICK, 100);
                    p.setData(reason.getBytes(StandardCharsets.UTF_8));
                    connection.queuePacket(p);
                    break;
                }
            }
        } else {
            sender.sendMessage("Invalid args[1]");
        }
    }
}
