package me.committee.api.config;

public class HudElementConfig {
    public boolean enabled;
    public float x;
    public float y;
    public float width;
    public float height;

    public HudElementConfig(boolean enabled, float x, float y, float width, float height) {
        this.enabled = enabled;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
