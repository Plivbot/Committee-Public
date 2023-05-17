package me.committee.impl.commands;

import me.committee.Committee;
import me.committee.api.command.Command;
import me.committee.packetapi.Packet;

import java.nio.charset.StandardCharsets;

public class ConnectionCommand extends Command {

    public ConnectionCommand() {
        super("Connection", new String[]{}, "Manages the connection to the backend.");
    }

    @Override
    public String[][] getCompletions() {
        return new String[][]{
                {"connect"},
                {"disconnect"},
                {"togglechat"},
                {"login", "[STRING]", "[STRING]"},
                {"register", "[STRING]", "[STRING]"}
        };
    }

    @Override
    public void onExec(String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("connect")) {
                Committee.backendConnectionManager.connect("127.0.0.1", 6666);
            } else if (args[0].equalsIgnoreCase("disconnect")) {
                Committee.backendConnectionManager.disconnect();
            } else if (args[0].equalsIgnoreCase("togglechat")) {
                Committee.backendConnectionManager.toggleChat();
            } else if (args[0].equalsIgnoreCase("login")) {

                if (args[2] == null) {
                    notEnoughArgs();
                }

                String loginString = args[1] + ":" + args[2];

                Packet loginPacket = new Packet(Packet.AUTH, loginString.getBytes(StandardCharsets.UTF_8).length);
                loginPacket.setData(loginString.getBytes(StandardCharsets.UTF_8));
                Committee.backendConnectionManager.sendPacket(loginPacket);
            } else if (args[0].equalsIgnoreCase("register")) {
                // todo: actual auth, rn any credentials are ok
            } else {
                invalidArg(0);
            }
        } else {
            notEnoughArgs();
        }
    }
}
