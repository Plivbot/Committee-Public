package me.committee.impl.commands;

import me.committee.Committee;
import me.committee.api.command.Command;
import me.committee.api.player.PlayerEnemy;
import me.committee.api.util.MessageSendHelper;

public class EnemyCommand extends Command {

    public EnemyCommand() {
        super("Enemy", new String[]{"E"}, "Add/Remove enemies.");
    }

    @Override
    public String[][] getCompletions() {
        return new String[][]{};
    }

    @Override
    public void onExec(String[] args) {
        if (args.length < 1) {
            MessageSendHelper.sendMessage("Not enough args.", MessageSendHelper.Level.WARN);
            return;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("clear")) {
                Committee.playerManager.getEnemies().clear();
                MessageSendHelper.sendMessage("Cleared enemy list.", MessageSendHelper.Level.INFO);
            } else if (args[0].equalsIgnoreCase("list")) {
                if (Committee.playerManager.getEnemies().size() > 0) {
                    MessageSendHelper.sendMessage("Enemy list:");
                    for (PlayerEnemy enemy : Committee.playerManager.getEnemies()) {
                        MessageSendHelper.sendMessage(enemy.getName(), MessageSendHelper.PrefixType.NONE);
                    }
                } else {
                    MessageSendHelper.sendMessage("You have no enemies on your enemy list.");
                }
            } else {
                final String name = args[0];
                if (Committee.playerManager.addEnemy(new PlayerEnemy(name)))
                    MessageSendHelper.sendMessage("\u00a7l" + name + "\u00a7r has been \u00a7aadded\u00a7r to your enemy list. >:)", MessageSendHelper.Level.INFO);
                else
                    MessageSendHelper.sendMessage("Could not add the person \u00a7l" + name + "\u00a7r to your enemy list.", MessageSendHelper.Level.INFO);
            }
        } else {
            final String name = args[1];
            if (args[0].equalsIgnoreCase("add")) {
                if (Committee.playerManager.addEnemy(new PlayerEnemy(name)))
                    MessageSendHelper.sendMessage("\u00a7l" + name + "\u00a7r has been \u00a7aadded\u00a7r to your enemy list. >:)", MessageSendHelper.Level.INFO);
                else
                    MessageSendHelper.sendMessage("Could not add the person \u00a7l" + name + "\u00a7r to your enemy list.", MessageSendHelper.Level.INFO);
            } else if (args[0].equalsIgnoreCase("del")) {
                if (Committee.playerManager.removeEnemyByName(name))
                    MessageSendHelper.sendMessage("\u00a7l" + name + "\u00a7r has been \u00a7cremoved\u00a7r to your enemy list. :)", MessageSendHelper.Level.INFO);
                else
                    MessageSendHelper.sendMessage("Could not find a person on your enemy list with the name \u00a7l" + name + "\u00a7r.", MessageSendHelper.Level.INFO);
            }
        }

    }
}
