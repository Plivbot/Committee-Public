package me.committee.impl.commands;

import me.committee.Committee;
import me.committee.api.command.Command;
import me.committee.api.util.MessageSendHelper;

public class CommandsCommand extends Command {
    public CommandsCommand() {
        super("Commands", new String[]{}, "Lists all the commands.");
    }

    @Override
    public String[][] getCompletions() {
        return new String[0][];
    }

    @Override
    public void onExec(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Command list:\n");

        for (Command command : Committee.commandManager.commands) {
            stringBuilder.append(command.getName()).append("\n");
        }

        MessageSendHelper.sendMessage(stringBuilder.toString());
    }
}
