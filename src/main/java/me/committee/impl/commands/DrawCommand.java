package me.committee.impl.commands;

import me.committee.Committee;
import me.committee.api.command.Command;
import me.committee.api.module.Module;
import me.committee.api.util.MessageSendHelper;

public class DrawCommand extends Command {
    public DrawCommand() {
        super("Draw", new String[]{"D"}, "Hides or shows modules on arraylist");
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

        if (module.isHidden()) {
            MessageSendHelper.sendMessage(module.getName() + "is \u00A7ashown\u00A7f on list");
            module.setHidden(false);
        }
        else {
            MessageSendHelper.sendMessage(module.getName() + "is \u00A7chidden\u00A7f on list");
            module.setHidden(true);
        }
    }
}
