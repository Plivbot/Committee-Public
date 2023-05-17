package me.committee.impl.modules.client;

import me.committee.Committee;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import org.lwjgl.input.Keyboard;

public class ClickGUI extends Module {

    public ClickGUI() {
        super("ClickGUI", new String[]{}, "Toggle modules in a GUI.", Keyboard.KEY_RSHIFT, Category.CLIENT);
    }

    private final Setting<Boolean> snapSliders = new Setting<>("SnapSliders", "Makes sliders snap every x values (not working yet)", true).setID(0);
    private final Setting<Boolean> renderDescriptions = new Setting<>("RenderDescriptions", "Renders descriptions like this one in the ClickGUI", true).setID(1);
    private final Setting<Integer> descriptionDelayms = new Setting<>("DescriptionDelay", "Delay in ms until the description starts rendering", 0, 0, 5000, 1000).setID(2);
    private final Setting<Boolean> settingNameCutoff = new Setting<>("SettingNameCutoff", "Cuts of the setting names so they don't render outside of the setting box", true).setID(3);

    @Override
    public void onEnable() {
        super.onEnable();
        mc.displayGuiScreen(Committee.committeeClickGui);
        this.toggle();
    }
}

