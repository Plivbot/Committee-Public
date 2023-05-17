package me.committee.backend.commands.senders;

public class ConsoleCommandSender implements CommandSender {
    @Override
    public void sendMessage(String message) {
        System.out.println(message);
    }

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public int getPermissionLevel() {
        return 10;
    }
}
