package me.committee.api.module;

import me.committee.Committee;
import me.committee.api.feature.ToggleableFeature;
import org.lwjgl.input.Keyboard;

public abstract class Module extends ToggleableFeature {

    private final Category category;

    private int keyBind = Keyboard.KEY_NONE;

    private boolean hidden = false;

    public Module(String name, String[] alias, String desc, Category category) {
        super(name, alias, desc);
        this.category = category;
    }

    public Module(String name, String[] alias, String desc, int keyBind, Category category) {
        super(name, alias, desc);
        this.keyBind = keyBind;
        this.category = category;
    }

    public Module(String name, String[] alias, String desc, Category category, boolean hidden) {
        super(name, alias, desc);
        this.category = category;
        this.hidden = hidden;
    }

    public Module(String name, String[] alias, String desc, int keyBind, Category category, boolean hidden) {
        super(name, alias, desc);
        this.keyBind = keyBind;
        this.category = category;
        this.hidden = hidden;
    }

    @Override
    public void onEnable() {
        Committee.LOGGER.debug("subscribing " + getName() + " to the event bus");
        Committee.EVENT_BUS.subscribe(this);
    }

    @Override
    public void onDisable() {
        Committee.EVENT_BUS.unsubscribe(this);
    }

    public Category getCategory() {
        return category;
    }

    public int getKeyBind() {
        return keyBind;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public enum Category {
        COMBAT("Combat"),
        MOVEMENT("Movement"),
        MISC("Misc."),
        WORLD("World"),
        EXPLOIT("Exploit"),
        RENDER("Render"),
        CLIENT("Client");

        final String readableName;

        Category(String betterName) {
            this.readableName = betterName;
        }

        public String getReadableName() {
            return readableName;
        }

    }
    public int getRotationPriority() {
        return 50;
    }
}
