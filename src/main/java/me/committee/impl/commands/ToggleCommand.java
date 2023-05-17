package me.committee.impl.commands;

import me.committee.Committee;
import me.committee.api.command.Command;
import me.committee.api.module.Module;
import me.committee.api.util.MessageSendHelper;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("Toggle", new String[]{"T"}, "Toggles modules on and off.");
    }

    @Override
    public String[][] getCompletions() {
        return new String[][]
                {
                        {"[MODULE]"}
                };
    }

    @Override
    public void onExec(String[] args) {

        if (args.length < 1) {
            MessageSendHelper.sendMessage("Not enough args.", MessageSendHelper.Level.WARN);
            return;
        }

        Module module = Committee.moduleManager.getModuleByName(args[0]);

        if (module == null) {
            MessageSendHelper.sendMessage("Invalid module.", MessageSendHelper.Level.WARN);
            return;
        }

        module.toggle();
    }
}
