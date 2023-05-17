package me.committee.backend.commands.commands;

import me.committee.backend.Server;
import me.committee.backend.commands.Command;
import me.committee.backend.commands.senders.CommandSender;

public class CommandsCommand extends Command {
    public CommandsCommand() {
        super("commands");
    }

    @Override
    public void onExec(String[] args, CommandSender sender) {
        StringBuilder sb = new StringBuilder("List of commands:\n");

        for (Command command : Server.commandManager.getCommands()) {
            sb.append(command.getName()).append("\n");
        }

        sender.sendMessage(sb.toString());
    }
}
