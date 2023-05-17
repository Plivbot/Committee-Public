package me.committee.backend.commands;

import me.committee.backend.commands.senders.CommandSender;

public abstract class Command {
    private final String name;

    public Command(String name) {
        this.name = name;
    }

    public abstract void onExec(String[] args, CommandSender sender);

    public String getName() {
        return name;
    }
}
