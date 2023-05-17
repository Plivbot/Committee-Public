package me.committee.api.gui.hud.elements;

import me.committee.api.feature.ToggleableFeature;
import net.minecraft.client.gui.ScaledResolution;

public abstract class HudElement extends ToggleableFeature {

    private float x;
    private float y;
    private float width;
    private float height;

    private boolean dragging;

    public HudElement(String name, String[] alias, String desc, float x, float y, float width, float height) {
        super(name, alias, desc);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dragging = false;
    }

    /**
     * If you use this you need to set the width & height when the element first renders!!!
     * Otherwise, the element will be only able to be dragged / interacted from a 10x10px area in the top-left corner
     *
     * @param name
     * @param alias
     * @param desc
     * @param x
     * @param y
     */
    public HudElement(String name, String[] alias, String desc, float x, float y) {
        super(name, alias, desc);
        this.x = x;
        this.y = y;
        this.width = 10;
        this.height = 10;
        this.dragging = false;
    }

    public abstract void render(float partialTicks);

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setXNoOffscreen(float x) {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        if (x < 0) this.x = 0;
        else this.x = Math.min(x, scaledResolution.getScaledWidth() - this.width);
    }

    public void setYNoOffscreen(float y) {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        if (y < 0) this.y = 0;
        else this.y = Math.min(y, scaledResolution.getScaledHeight() - this.height);
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }
}
