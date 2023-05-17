package me.committee.backend.commands;

import me.committee.backend.commands.senders.ConsoleCommandSender;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public void init() {
        Reflections reflections = new Reflections("me.committee.backend.commands.commands");

        for (Class<? extends Command> aClass : reflections.getSubTypesOf(Command.class)) {
            try {
                System.out.printf("Adding command %s%n", aClass.getCanonicalName());
                commands.add(aClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void parseAndExec(String in) {

        // args[0] is the command name!!!
        String[] args = in.split("[ ]+");

        if (args.length == 0) {
            return;
        }

        for (Command command : commands) {
            if (command.getName().equalsIgnoreCase(args[0])) {
                command.onExec(args, new ConsoleCommandSender());
                return;
            }
        }

        System.out.println("Invalid command, get the list of commands with the command \"commands\"");
    }

    public List<Command> getCommands() {
        return commands;
    }
}
