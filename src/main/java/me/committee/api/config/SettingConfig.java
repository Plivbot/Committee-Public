package me.committee.api.config;

public class SettingConfig<T> {

    public String name;
    public T value;

    public SettingConfig(String name, T value) {
        this.name = name;
        this.value = value;
    }
}
