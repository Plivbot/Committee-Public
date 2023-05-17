package me.committee.api.feature;

import me.committee.Committee;
import me.committee.api.setting.Setting;
import me.committee.api.util.MessageSendHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ToggleableFeature extends Feature {

    private boolean enabled = false;
    private boolean sendToggleMessage = true;

    private final Map<Integer, Setting<?>> settings = new HashMap<>();

    public ToggleableFeature(String name, String[] alias, String desc) {
        super(name, alias, desc);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {

            if (sendToggleMessage && Committee.INSTANCE.isLoaded) {
                MessageSendHelper.sendMessage(getName() + " " + (this.enabled ? "\u00a7cdisabled" : "\u00a7aenabled"));
            }

            this.enabled = enabled;

            if (this.enabled) onEnable();
            else this.onDisable();
        }
    }

    public void toggle() {
        this.setEnabled(!this.isEnabled());
    }

    public void setSendToggleMessage(boolean sendMessages) {
        sendToggleMessage = sendMessages;
    }

    public boolean getToggleMessages() {
        return sendToggleMessage;
    }

    public Setting<?> getSettingById(int id) {
        return settings.get(id);
    }

    @Deprecated
    public Setting<?> getSettingByName(String name) {
        return getSettingByName(name, true);
    }

    @Deprecated
    public Setting<?> getSettingByName(String name, boolean useAlias) {

        final AtomicReference<Setting<?>> returnValue = new AtomicReference<>();

        settings.forEach((integer, setting) -> {
            if (setting.getName().equalsIgnoreCase(name)) {
                returnValue.set(setting);
            }

            if (useAlias) {
                for (String alias : setting.getAlias()) {
                    if (alias.equalsIgnoreCase(name)) {
                        returnValue.set(setting);
                    }
                }
            }
        });
        return returnValue.get();
    }

    public Map<Integer, Setting<?>> getSettings() {
        return settings;
    }

    public List<Setting<?>> getSettingsAsList() {
        final List<Setting<?>> list = new ArrayList<>();
        settings.forEach((integer, setting) -> {
            list.add(setting);
        });

        return list;
    }

    public abstract void onEnable();

    public abstract void onDisable();
}
