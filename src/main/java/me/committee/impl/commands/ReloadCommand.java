package me.committee.impl.commands;

import me.committee.Committee;
import me.committee.api.command.Command;
import me.committee.api.util.MessageSendHelper;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("Reload", new String[]{}, "Saves the config and reloads the managers for the client, used for debugging");
    }

    @Override
    public String[][] getCompletions() {
        return new String[][]{
                {}
        };
    }

    @Override
    public void onExec(String[] args) {
        MessageSendHelper.sendMessage("Reloading the client...", MessageSendHelper.Level.WARN);
        Committee.INSTANCE.unLoad();
        Committee.INSTANCE.load();
    }
}
