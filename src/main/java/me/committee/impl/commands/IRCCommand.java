package me.committee.impl.commands;

import me.committee.Committee;
import me.committee.api.command.Command;
import me.committee.api.util.MessageSendHelper;
import me.committee.packetapi.Packet;

import java.nio.charset.StandardCharsets;

public class IRCCommand extends Command {

    public IRCCommand() {
        super("IRC", new String[]{}, "Command to send chat messages in the irc chat.");
    }

    @Override
    public void onExec(String[] args) {
        if (Committee.backendConnectionManager.isConnected()) {

            if (args.length > 0) {


                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < args.length - 1; i++) {
                    sb.append(args[i]).append(" ");
                }
                sb.replace(sb.length(), sb.length(), "");

                byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);

                Packet p = new Packet(Packet.CHAT, bytes.length);
                p.setData(bytes);

                Committee.backendConnectionManager.sendPacket(p);
            } else {
                MessageSendHelper.sendMessage("You may also want to send some characters in the message?");
            }
        } else {
            MessageSendHelper.sendMessage("The backend is disconnected, please connect with the commands " +
                    "\"" + Command.PREFIX + "connection connect\" and \"" + Command.PREFIX + "connection togglechat\"" +
                    "to begin using the chat service!", MessageSendHelper.Level.WARN);
        }
    }

    @Override
    public String[][] getCompletions() {
        return new String[][]{
                {"[STRING]"}
        };
    }
}
