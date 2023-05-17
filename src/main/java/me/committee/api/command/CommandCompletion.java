package me.committee.api.command;

import me.committee.Committee;
import me.committee.api.module.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CommandCompletion {

    private final Command command;

    public CommandCompletion(Command command) {
        this.command = command;
    }

    /**
     * Gets the next completion
     *
     * @param currentText the text typed in chat, prefix and the command name/alias parsed away
     * @param tabs        how many times the user has pressed the tab button
     *                    if you have pressed space after the first argument and have 2 possible completions
     *                    of which the other one has a second argument it will complete to the one with the
     *                    second argument, no matter how many times you spam tab
     * @return the completions
     */
    public String getNext(String currentText, int tabs) {

        // todo: make this not scuffed and add this to chat

        boolean endsSpace = currentText.endsWith(" ");
        String[] split = currentText.split("[ ]+");

        StringBuilder splitBuilder = new StringBuilder();
        for (String s : split) {
            splitBuilder.append(s);
        }

        List<String> completions = new ArrayList<>();


        for (String[] completion : command.getCompletions()) {
            // todo: this tries to recommend all next args, instead of only the ones that have fitting args before them
            StringBuilder completionJoined = new StringBuilder();

            Iterator<String> completionIterator = Arrays.stream(split).iterator();
            while (completionIterator.hasNext()) {
                completionJoined.append(completionIterator.next());
            }

            if (completion.length >= split.length) {

                boolean escapingString = false;
                int offset = 0;

                for (int i = 0; i < split.length; i++) {
                    String s = split[i];
                    Committee.LOGGER.debug("split length: {}, i: {}, split: {}", split.length, i, s);
                    String compl = completion[i - offset];
                    if (endsSpace) {
                        compl = "";
                    }

                    if (compl.equals("[STRING]")) {
                        // todo: allow escaping with \"
                        if (s.endsWith("\"") && escapingString) {
                            escapingString = false;
                        } else {
                            offset++;
                        }

                        if (s.startsWith("\"")) {
                            escapingString = true;
                        }
                    } else if (compl.equals("[MODULE]")) {
                        // todo: also search module list by alias
                        List<Module> modules = Committee.moduleManager.getModules().stream().filter(m ->
                                m.getName().toLowerCase().startsWith(s.toLowerCase())).collect(Collectors.toList());

                        for (Module m : modules) {
                            completions.add(m.getName().replace(split[split.length - 1], ""));
                        }
                    } else if (compl.equals("") && endsSpace && completion.length > split.length && completion[i - offset].toLowerCase().startsWith(split[i - offset].toLowerCase())) {

                        if (!completions.contains(completion[i - offset + 1])) {
                            Committee.LOGGER.debug("compl: {}", completion[i - offset + 1]);
                            completions.add(completion[i - offset + 1]);
                        }

                    } else if (compl.startsWith(s)) {

                        if (i == split.length - 1) {
                            completions.add(compl.replaceFirst(split[split.length - 1], ""));
                        }

                    } else { // todo: support for settings
                        // don't add to completions
                    }

                }
            }
        }

        Committee.LOGGER.debug("tabs: {}, completions: {}, name: {}", tabs, completions.size(), command.getName());

        if (tabs > 0 && completions.size() > 1) {
            return completions.get(tabs % completions.size());
        } else if (completions.isEmpty()) {
            return "";
        } else {
            return completions.get(0);
        }
    }
}
