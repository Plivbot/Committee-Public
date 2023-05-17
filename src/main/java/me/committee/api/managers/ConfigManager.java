package me.committee.api.managers;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import me.committee.Committee;
import me.committee.api.command.Command;
import me.committee.api.config.*;
import me.committee.api.gui.clickgui.elements.CategoryComponent;
import me.committee.api.gui.hud.elements.HudElement;
import me.committee.api.module.Module;
import me.committee.api.setting.Colour;
import me.committee.api.setting.Setting;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigManager {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final File stuffFolder = new File("./" + Committee.MOD_ID);
    private final File lastConfigFile = new File(stuffFolder, "lastconfig");

    private final File configsFolder = new File(stuffFolder, "configs");
    private final File generalConfigFile = new File(stuffFolder, "config");

    private final String moduleLoc = "modules";
    private final String hudLoc = "hud";
    private final String clickGuiLoc = "clickgui";
    private final String playersLoc = "Players";

    private Set<String> configs;

    public void init() {
        configs = new HashSet<>();

        reloadConfigs();

        final String lastConfig = getLastConfig();

        loadConfig(ConfigType.MODULE, lastConfig);
        loadConfig(ConfigType.HUD, lastConfig);
        loadConfig(ConfigType.CLICK_GUI, lastConfig);
        loadConfig(ConfigType.PLAYERS, lastConfig);
        readGeneralConfig();
    }

    public void save() {
        final String lastConfig = getLastConfig();
        Committee.LOGGER.info("Saving module config...");
        write(ConfigType.MODULE, lastConfig);
        Committee.LOGGER.info("Saving hud config...");
        write(ConfigType.HUD, lastConfig);
        Committee.LOGGER.info("Saving clickgui config...");
        write(ConfigType.CLICK_GUI, lastConfig);
        Committee.LOGGER.info("Saving player config...");
        write(ConfigType.PLAYERS, lastConfig);
        Committee.LOGGER.info("Saving general config...");
        writeGeneralConfig();
    }

    public void writeGeneralConfig() {
        final GeneralConfig config = new GeneralConfig(Command.PREFIX);

        try {
            if (!this.generalConfigFile.exists())
                this.generalConfigFile.createNewFile();


            final BufferedWriter writer = Files.newWriter(this.generalConfigFile, StandardCharsets.UTF_8);
            writer.write(gson.toJson(config));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readGeneralConfig() {
        if (!this.generalConfigFile.exists())
            return;

        try {
            final BufferedReader reader = Files.newReader(this.generalConfigFile, StandardCharsets.UTF_8);
            final GeneralConfig config = gson.fromJson(reader, GeneralConfig.class);
            reader.close();

            Command.PREFIX = config.prefix;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLastConfig() {

        if (!this.lastConfigFile.exists()) {
            try {
                this.lastConfigFile.createNewFile();
            } catch (IOException ignored) {
            }

            try {
                final BufferedWriter writer = Files.newWriter(this.lastConfigFile, StandardCharsets.UTF_8);
                writer.write("default");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String returnValue = "default";

        try {
            final BufferedReader reader = Files.newReader(this.lastConfigFile, StandardCharsets.UTF_8);
            returnValue = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    public void reloadConfigs() {
        if (!this.configsFolder.exists())
            this.configsFolder.mkdirs();

        for (File file : this.configsFolder.listFiles()) {
            this.configs.add(file.getName());
        }

        if (this.configsFolder.listFiles().length == 0)
            this.configs.add("default");
    }

    public IOStatus loadConfig(ConfigType type, String name) {

        final File configFile = new File(configsFolder, name);
        if (!configFile.exists())
            configFile.mkdir();

        BufferedReader reader;
        if (type == ConfigType.MODULE) {
            for (Module module : Committee.moduleManager.getModules()) {
                try {
                    final File toRead = new File(configFile, moduleLoc + "/" + module.getName());
                    if (!toRead.exists())
                        continue;

                    Committee.LOGGER.debug("Reading config for module " + module.getName());
                    reader = Files.newReader(toRead, StandardCharsets.UTF_8);
                    final ModuleConfig config = gson.fromJson(reader, ModuleConfig.class);

                    for (Setting<?> moduleSetting : ImmutableList.copyOf(module.getSettingsAsList())) {
                        config.settings.forEach(configSetting -> {
                            if (moduleSetting.getName().equals(configSetting.name)) {
                                if (configSetting.value instanceof Double) {
                                    final Double setting = (Double) configSetting.value;
                                    if (moduleSetting.getValue() instanceof Integer)
                                        ((Setting<Integer>) moduleSetting).setValue(setting.intValue());
                                    else if (moduleSetting.getValue() instanceof Float)
                                        ((Setting<Float>) moduleSetting).setValue(setting.floatValue());
                                } else if (moduleSetting.getValue() instanceof Boolean && configSetting.value instanceof Boolean)
                                    ((Setting<Boolean>) moduleSetting).setValue((Boolean) configSetting.value);
                                else if (moduleSetting.getValue() instanceof String && configSetting.value instanceof String)
                                    ((Setting<String>) moduleSetting).setValue((String) configSetting.value);
                                else if (moduleSetting.getValue() instanceof Enum && configSetting.value instanceof String)
                                    moduleSetting.setValueOfEnum((String) configSetting.value);
                                else if (configSetting.value instanceof LinkedTreeMap) {
                                    if (moduleSetting.getValue() instanceof Colour) {
                                        final LinkedTreeMap<?, ?> linkedTreeMap = (LinkedTreeMap<?, ?>) configSetting.value;
                                        if (linkedTreeMap.values().stream().allMatch(item -> item instanceof Double)) {
                                            if (linkedTreeMap.get("red") != null &&
                                                    linkedTreeMap.get("green") != null &&
                                                    linkedTreeMap.get("blue") != null &&
                                                    linkedTreeMap.get("alpha") != null
                                            ) {
                                                final Double red = (Double) linkedTreeMap.get("red");
                                                final Double green = (Double) linkedTreeMap.get("green");
                                                final Double blue = (Double) linkedTreeMap.get("blue");
                                                final Double alpha = (Double) linkedTreeMap.get("alpha");

                                                final Colour colour = (Colour) moduleSetting.getValue();
                                                colour.setRed(red.intValue());
                                                colour.setGreen(green.intValue());
                                                colour.setBlue(blue.intValue());
                                                colour.setAlpha(alpha.intValue());
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }

                    module.setSendToggleMessage(config.enableMessages);

                    module.setKeyBind(config.keyBind);

                    Committee.LOGGER.debug("enabled: " + config.enabled);

                    if (config.enabled)
                        module.setEnabled(true);

                    if (config.hidden)
                        module.setHidden(true);

                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (type == ConfigType.HUD) {
            for (HudElement element : Committee.hudElementManager.getElements()) {
                try {
                    final File toRead = new File(configFile, hudLoc + "/" + element.getName());
                    if (!toRead.exists())
                        continue;

                    Committee.LOGGER.debug("Reading config for Hud Element " + element.getName());
                    reader = Files.newReader(toRead, StandardCharsets.UTF_8);
                    HudElementConfig config = gson.fromJson(reader, HudElementConfig.class);

                    element.setEnabled(config.enabled);
                    element.setX(config.x);
                    element.setY(config.y);
                    element.setWidth(config.width);
                    element.setHeight(config.height);

                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (type == ConfigType.CLICK_GUI) {
            for (CategoryComponent element : Committee.clickGuiElementManager.getElements()) {
                try {
                    final File toRead = new File(configFile, clickGuiLoc + "/" + element.getName());
                    if (!toRead.exists())
                        continue;

                    Committee.LOGGER.debug("Reading config for clickgui category element " + element.getName());
                    reader = Files.newReader(toRead, StandardCharsets.UTF_8);
                    GuiElementConfig config = gson.fromJson(reader, GuiElementConfig.class);

                    element.setRelX(config.x);
                    element.setRelY(config.y);
                    element.setOpen(config.open);

                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (type == ConfigType.PLAYERS) {
            try {
                final File toRead = new File(configFile, playersLoc);
                if (!toRead.exists())
                    return IOStatus.NOT_FOUND;


                reader = Files.newReader(toRead, StandardCharsets.UTF_8);
                final PlayerConfig config = gson.fromJson(reader, PlayerConfig.class);

                Committee.playerManager.getFriends().clear();
                Committee.playerManager.getEnemies().clear();

                Committee.playerManager.getFriends().addAll(config.playerFriends);
                Committee.playerManager.getEnemies().addAll(config.playerEnemies);

                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return IOStatus.SUCCESS;
    }

    public IOStatus write(ConfigType type, String name) {

        final File configFile = new File(this.configsFolder, name);
        if (!configFile.exists()) configFile.mkdir();

        final File modulesFile = new File(configFile, moduleLoc);
        if (!modulesFile.exists()) modulesFile.mkdir();

        final File hudFile = new File(configFile, hudLoc);
        if (!hudFile.exists()) hudFile.mkdir();

        final File clickGuiFile = new File(configFile, clickGuiLoc);
        if (!clickGuiFile.exists()) clickGuiFile.mkdir();

        BufferedWriter writer;

        if (type == ConfigType.MODULE) {
            for (Module module : Committee.moduleManager.getModules()) {
                try {
                    writer = Files.newWriter(new File(configFile, moduleLoc + "/" + module.getName()), StandardCharsets.UTF_8);
                } catch (FileNotFoundException e) {
                    return IOStatus.NOT_FOUND;
                }

                Committee.LOGGER.debug("Saving: " + module.getName() + " enabled: " + module.isEnabled());

                final ModuleConfig config = new ModuleConfig(
                        module.getToggleMessages(),
                        module.getSettingsAsList()
                                .stream()
                                .map(setting -> new SettingConfig(setting.getName(), setting.getValue()))
                                .collect(Collectors.toList()),
                        module.isEnabled(),
                        module.getKeyBind(),
                        module.isHidden()
                );

                try {
                    writer.write(gson.toJson(config));
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (type == ConfigType.HUD) {
            for (HudElement element : Committee.hudElementManager.getElements()) {
                try {
                    writer = Files.newWriter(new File(configFile, hudLoc + "/" + element.getName()), StandardCharsets.UTF_8);
                } catch (FileNotFoundException e) {
                    return IOStatus.NOT_FOUND;
                }

                Committee.LOGGER.debug("Saving: " + element.getName() + " enabled: " + element.isEnabled());
                final HudElementConfig config = new HudElementConfig(element.isEnabled(), element.getX(), element.getY(), element.getWidth(), element.getHeight());

                try {
                    writer.write(gson.toJson(config));
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (type == ConfigType.CLICK_GUI) {
            for (CategoryComponent element : Committee.clickGuiElementManager.getElements()) {
                try {
                    writer = Files.newWriter(new File(configFile, clickGuiLoc + "/" + element.getName()), StandardCharsets.UTF_8);
                } catch (FileNotFoundException e) {
                    return IOStatus.NOT_FOUND;
                }

                Committee.LOGGER.debug("Saving: " + element.getName());
                final GuiElementConfig config = new GuiElementConfig(element.getRelX(), element.getRelY(), element.isOpen());

                try {
                    writer.write(gson.toJson(config));
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (type == ConfigType.PLAYERS) {
            try {
                writer = Files.newWriter(new File(configFile, playersLoc), StandardCharsets.UTF_8);
            } catch (FileNotFoundException e) {
                return IOStatus.NOT_FOUND;
            }

            final PlayerConfig config = new PlayerConfig(Committee.playerManager.getFriends(), Committee.playerManager.getEnemies());

            try {
                writer.write(gson.toJson(config));
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return IOStatus.SUCCESS;
    }

    public enum ConfigType {
        MODULE("Module"),
        WAYPOINTS("Waypoints"),
        HUD("HUD"),
        CLICK_GUI("ClickGUI"),
        PLAYERS("Players");

        final String name;

        ConfigType(String strings) {
            this.name = strings;
        }
    }

    public enum IOStatus {
        NOT_FOUND("Unable to find that config."),
        CORRUPTED("The config is corrupted or somehow else unable to be read."),
        SUCCESS("Could load config.");

        final String explanation;

        IOStatus(String explanation) {
            this.explanation = explanation;
        }
    }
}
