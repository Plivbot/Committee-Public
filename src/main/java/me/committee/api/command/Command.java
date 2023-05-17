package me.committee.api.command;

import me.committee.api.feature.Feature;
import me.committee.api.util.MessageSendHelper;

public abstract class Command extends Feature {

    public static String PREFIX = "-";

    /**
     * Command constructor for data and stuff
     *
     * @param name  start with upper case
     * @param alias start with upper case
     * @param desc  Start with upper case and end with a dot.
     */
    public Command(String name, String[] alias, String desc) {
        super(name, alias, desc);
    }

    /**
     * Send this if the user sent an invalid argument in a command
     *
     * @param num the argument which is invalid
     */
    public static void invalidArg(int num) {
        MessageSendHelper.sendMessage("Invalid args[" + num + "]!!!", MessageSendHelper.Level.WARN);
    }

    /**
     * Send this if there are not enough arguments
     */
    public static void notEnoughArgs() {
        MessageSendHelper.sendMessage("You need more args smh!!! Look at the usage!", MessageSendHelper.Level.WARN);
    }

    public abstract String[][] getCompletions();

    public abstract void onExec(String[] args);
}
