package me.committee.impl.event.events.input;

import me.committee.api.eventsystem.event.Event;
import me.committee.api.setting.Setting;

public class SettingChangeEvent<T> extends Event {

    private Setting<T> setting;
    private T oldValue;

    public SettingChangeEvent(Setting<T> setting, T oldValue) {
        this.setting = setting;
        this.oldValue = oldValue;
    }

    public Setting<T> getSetting() {
        return setting;
    }

    public T getOldValue() {
        return oldValue;
    }
}
