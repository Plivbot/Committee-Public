package me.committee.backend.commands.senders;

public interface CommandSender {

    void sendMessage(String message);

    boolean isServer();

    int getPermissionLevel();
}
