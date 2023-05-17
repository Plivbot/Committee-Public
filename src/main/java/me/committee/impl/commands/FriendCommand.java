package me.committee.impl.commands;

import me.committee.Committee;
import me.committee.api.command.Command;
import me.committee.api.player.PlayerFriend;
import me.committee.api.util.MessageSendHelper;

public class FriendCommand extends Command {

    public FriendCommand() {
        super("Friend", new String[]{"F"}, "Add/Remove friends.");
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
                Committee.playerManager.getFriends().clear();
                MessageSendHelper.sendMessage("Cleared friends list. :(", MessageSendHelper.Level.INFO);
            } else if (args[0].equalsIgnoreCase("list")) {
                if (Committee.playerManager.getFriends().size() > 0) {
                    MessageSendHelper.sendMessage("Friends list:");
                    for (PlayerFriend friend : Committee.playerManager.getFriends()) {
                        MessageSendHelper.sendMessage(friend.getName(), MessageSendHelper.PrefixType.NONE);
                    }
                } else {
                    MessageSendHelper.sendMessage("You have no friends on your friends list. :(");
                }
            } else {
                final String name = args[0];
                if (Committee.playerManager.addFriend(new PlayerFriend(name)))
                    MessageSendHelper.sendMessage("\u00a7l" + name + "\u00a7r has been \u00a7aadded\u00a7r to your friends list. :)", MessageSendHelper.Level.INFO);
                else
                    MessageSendHelper.sendMessage("Could not add the person \u00a7l" + name + "\u00a7r to your friends list. :(", MessageSendHelper.Level.INFO);
            }
        } else {
            final String name = args[1];
            if (args[0].equalsIgnoreCase("add")) {
                if (Committee.playerManager.addFriend(new PlayerFriend(name)))
                    MessageSendHelper.sendMessage("\u00a7l" + name + "\u00a7r has been \u00a7aadded\u00a7r to your friends list. :)", MessageSendHelper.Level.INFO);
                else
                    MessageSendHelper.sendMessage("Could not add the person \u00a7l" + name + "\u00a7r to your friends list. :(", MessageSendHelper.Level.INFO);

            } else if (args[0].equalsIgnoreCase("del")) {
                if (Committee.playerManager.removeFriendByName(name))
                    MessageSendHelper.sendMessage("\u00a7l" + name + "\u00a7r has been \u00a7cremoved\u00a7r to your friends list. :(", MessageSendHelper.Level.INFO);
                else
                    MessageSendHelper.sendMessage("Could not find a person on your friends list with the name \u00a7l" + name + "\u00a7r.", MessageSendHelper.Level.INFO);
            }
        }

    }
}
