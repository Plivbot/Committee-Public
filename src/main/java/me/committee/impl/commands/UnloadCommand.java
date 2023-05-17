package me.committee.impl.commands;

import me.committee.Committee;
import me.committee.api.command.Command;
import me.committee.api.util.MessageSendHelper;

public class UnloadCommand extends Command {


    public UnloadCommand() {
        super("Unload", new String[]{}, "Unloads the client");
    }

    @Override
    public String[][] getCompletions() {
        return new String[][]{
                {}
        };
    }

    @Override
    public void onExec(String[] args) {
        MessageSendHelper.sendMessage("Unloading the client...", MessageSendHelper.Level.WARN);
        MessageSendHelper.sendMessage("If you want to clear the chat, press f3 + d");
        Committee.INSTANCE.unLoad();
    }
}
