package me.committee.api.config;

import java.util.List;

public class ModuleConfig {

    public boolean enableMessages;
    public List<SettingConfig> settings;
    public boolean enabled;
    public int keyBind;

    public boolean hidden;

    public ModuleConfig(boolean enableMessages, List<SettingConfig> settings, boolean enabled, int keyBind, boolean hidden) {
        this.enableMessages = enableMessages;
        this.settings = settings;
        this.enabled = enabled;
        this.keyBind = keyBind;
        this.hidden = hidden;
    }
}
