package me.committee.backend.commands.commands;

import me.committee.backend.Server;
import me.committee.backend.commands.Command;
import me.committee.backend.commands.senders.CommandSender;

public class ShutdownCommand extends Command {
    public ShutdownCommand() {
        super("shutdown");
    }

    @Override
    public void onExec(String[] args, CommandSender sender) {
        Server.shuttingDown.set(true);
    }
}
