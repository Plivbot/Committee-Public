package me.committee.api.managers;

import me.committee.Committee;
import me.committee.api.command.Command;
import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Colour;
import me.committee.api.setting.Setting;
import me.committee.api.util.MathUtil;
import me.committee.api.util.MessageSendHelper;
import me.committee.impl.event.events.chat.ChatEvent;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CommandManager {

    public static final int MAX_SETTING_LENGTH = 16;
    public List<Command> commands;

    public void init() {
        this.commands = new ArrayList<>();

        Committee.LOGGER.info("Loading commands...");

        Set<Class<? extends Command>> classes = new Reflections("me.committee.impl.commands").getSubTypesOf(Command.class);

        for (Class<?> clazz : classes) {
            try {
                Committee.LOGGER.debug("Adding: " + clazz);
                this.commands.add((Command) clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Committee.LOGGER.info("Commands initialized.");
    }

    private Command getCommandByClass(Class<? extends Command> clazz) {
        for (Command command : this.commands) {
            if (command.getClass() == clazz)
                return command;
        }
        return null;
    }

    private Command getCommandByNameOrAlias(String name) {
        for (Command command : this.commands) {
            if (command.getName().equalsIgnoreCase(name)) return command;
            for (String alias : command.getAlias()) {
                if (alias.equalsIgnoreCase(name)) return command;
            }
        }
        return null;
    }

    @EventSubscribe
    public void onChat(ChatEvent event) {
        if (!event.getMessage().startsWith(Command.PREFIX)) {
            return;
        }

        event.setCancelled(true);

        Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(event.getMessage());

        final String[] split = event.getMessage().split("[ ]+");
        final String command = split[0].replaceFirst(Command.PREFIX, "");

        final String[] onlyArgs = Arrays.copyOfRange(split, 1, split.length);

        final Command c = getCommandByNameOrAlias(command);

        if (c != null) {
            c.onExec(onlyArgs);
            return;
        }


        for (Module module : Committee.moduleManager.getModules()) {
            if (this.isCommandAModule(command, module)) {
                setSettings(onlyArgs, module);
                return;
            }
        }

        MessageSendHelper.sendMessage("Could not find a command with the name \"" + command + "\"." +
                " List all the commands with the command \"" + Command.PREFIX + "commands" + "\" or type a module's name" +
                "to toggle it.", MessageSendHelper.Level.WARN);
    }

    /**
     * setSettings
     * <p>
     * Sets the defined {@link Setting} for the {@link me.committee.api.feature.ToggleableFeature} by parsing the string
     * array provided
     *
     * @param onlyArgs
     * @param module
     * @return did we find and change a setting (was the value type also correct)
     */
    @SuppressWarnings("unchecked")
    private boolean setSettings(String[] onlyArgs, Module module) {
        if (onlyArgs.length == 0) {
            module.toggle();
        } else {
            // todo: we need a manager for this stuff
            if (onlyArgs[0].equalsIgnoreCase("bind")) {
                final int key = Keyboard.getKeyIndex(onlyArgs[1].toUpperCase());
                module.setKeyBind(key);
                if (key == Keyboard.KEY_NONE) {
                    MessageSendHelper.sendMessage("Set keybinding of " + module.getName() + " to NONE.", MessageSendHelper.Level.WARN);
                } else {
                    MessageSendHelper.sendMessage("Set keybinding of " + module.getName() + " to " + onlyArgs[1].toUpperCase() + ".", MessageSendHelper.Level.INFO);
                }
            } else if (onlyArgs[0].equalsIgnoreCase("list")) {
                listSettings(module);
            } else {
                final Setting<?> setting = module.getSettingByName(onlyArgs[0]);

                if (setting == null) {
                    listSettings(module);
                } else {
                    // todo: handle enums in a better way, can't be bothered rn
                    if (onlyArgs.length > 1 || setting.getValue() instanceof Enum) {
                        if (setting.getValue() instanceof Boolean) {
                            ((Setting<Boolean>) setting).setValue(Boolean.parseBoolean(onlyArgs[1]));
                            final Boolean settingValue = (Boolean) setting.getValue();
                            MessageSendHelper.sendMessage("Set value " + setting.getName() + " to " + (settingValue ? "\u00a7a" : "\u00a7c") + settingValue + ".", MessageSendHelper.Level.INFO);
                            return true;
                        } else if (setting.getValue() instanceof String) {
                            final String text = String.join(" ", Arrays.copyOfRange(onlyArgs, 1, onlyArgs.length));
                            ((Setting<String>) setting).setValue(text);
                            MessageSendHelper.sendMessage("Set value " + setting.getName() + " to " + text + ".", MessageSendHelper.Level.INFO);
                            return true;
                        } else if (setting.getValue() instanceof Enum) {
                            if (onlyArgs.length > 1 && setting.setValueOfEnum(onlyArgs[1])) {
                                MessageSendHelper.sendMessage("Set value " + setting.getName() + " to " + setting.getValue() + ".", MessageSendHelper.Level.INFO);
                            } else {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Possible enum values:");
                                for (Enum<?> possibleEnumValue : setting.getPossibleEnumValues()) {
                                    sb.append("\n").append(possibleEnumValue.name());
                                }
                                MessageSendHelper.sendMessage(sb.toString());
                            }
                            return true;
                        } else if (setting.getValue() instanceof Colour) {
                            if (onlyArgs.length >= 4) { // module setting r g b a OR // module setting r g b
                                try {
                                    int red = Integer.parseInt(onlyArgs[1]);
                                    int green = Integer.parseInt(onlyArgs[2]);
                                    int blue = Integer.parseInt(onlyArgs[3]);
                                    int alpha = onlyArgs.length >= 5 ? Integer.parseInt(onlyArgs[4]) : 255;
                                    final Colour colour = new Colour(red, green, blue, alpha);
                                    ((Setting<Colour>) setting).setValue(colour);
                                    MessageSendHelper.sendMessage("Set value " + setting.getName() + " to " + colour + ".", MessageSendHelper.Level.INFO);
                                } catch (NumberFormatException e) {
                                    MessageSendHelper.sendMessage("Invalid value for setting.", MessageSendHelper.Level.WARN);
                                    return false;
                                }
                                return true;
                            } else {
                                try {
                                    long hexColour = Long.decode(onlyArgs[1]);
                                    final Colour colour = new Colour(hexColour, true);
                                    colour.multiply(255);
                                    ((Setting<Colour>) setting).setValue(colour);
                                    MessageSendHelper.sendMessage("Set value " + setting.getName() + " to " + colour + ".", MessageSendHelper.Level.INFO);
                                } catch (NumberFormatException e) {
                                    MessageSendHelper.sendMessage("Invalid value for setting.", MessageSendHelper.Level.WARN);
                                    return false;
                                }
                                return true;
                            }
                        } else {
                            if (MathUtil.canParseNumber(onlyArgs[1])) {
                                try {
                                    if (setting.getValue() instanceof Integer)
                                        ((Setting<Integer>) setting).setValue(Integer.parseInt(onlyArgs[1]));
                                    else if (setting.getValue() instanceof Float)
                                        ((Setting<Float>) setting).setValue(Float.parseFloat(onlyArgs[1]));
                                    else {
                                        MessageSendHelper.sendMessage("No matching data format found!", MessageSendHelper.Level.WARN);
                                        return true;
                                    }
                                    MessageSendHelper.sendMessage("Set value " + setting.getName() + " to " + setting.getValue() + ".", MessageSendHelper.Level.INFO);
                                } catch (NumberFormatException e) {
                                    MessageSendHelper.sendMessage("Invalid value for setting.", MessageSendHelper.Level.WARN);
                                }
                                return true;
                            }
                        }
                        MessageSendHelper.sendMessage("Invalid value entered for setting " + setting.getName() + ".", MessageSendHelper.Level.WARN);
                    } else {
                        if (setting.getValue() instanceof Boolean) {
                            ((Setting<Boolean>) setting).setValue(!(Boolean) setting.getValue());
                            final Boolean settingValue = (Boolean) setting.getValue();
                            MessageSendHelper.sendMessage("Set value " + setting.getName() + " to " + (settingValue ? "\u00a7a" : "\u00a7c") + settingValue + ".", MessageSendHelper.Level.INFO);
                            return true;
                        } else if (setting.getValue() instanceof String) {
                            ((Setting<String>) setting).setValue("");
                            MessageSendHelper.sendMessage("Set value " + setting.getName() + " to an empty string.", MessageSendHelper.Level.INFO);
                            return true;
                        }
                        MessageSendHelper.sendMessage("No value given for setting " + setting.getName() + ".", MessageSendHelper.Level.WARN);
                    }
                }
            }
        }
        return false;
    }

    private void listSettings(Module module) {
        if (module.getSettings().size() > 0) {
            MessageSendHelper.sendMessage("Setting list:");
            module.getSettings().forEach((integer, moduleSetting) -> {
                final StringBuilder line = new StringBuilder(moduleSetting.getName());

                int length = MAX_SETTING_LENGTH - moduleSetting.getName().length();
                for (int i = 0; i < length; i++) {
                    line.append(" ");
                }

                MessageSendHelper.sendMessage(line.toString() + moduleSetting.getValue() + (moduleSetting.getValue() instanceof Float ? "f" : ""), MessageSendHelper.PrefixType.NONE);
            });
        } else {
            MessageSendHelper.sendMessage("This module has no settings!");
        }
    }

    private boolean isCommandAModule(String command, Module module) {
        if (module.getName().equalsIgnoreCase(command)) {
            return true;
        }

        for (String alias : module.getAlias()) {
            if (alias.equalsIgnoreCase(command)) {
                return true;
            }
        }
        return false;
    }

}
