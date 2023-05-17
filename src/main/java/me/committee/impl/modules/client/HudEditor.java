package me.committee.impl.modules.client;

import me.committee.Committee;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;

public class HudEditor extends Module {

    public HudEditor() {
        super("HudEditor", new String[]{"HudEdit"}, "Reposition elements for the Ingame Hud Overlay.", Category.CLIENT, true);
    }

    private final Setting<Float> snapPixels = new Setting<>("SnapPixels", new String[]{"SnapPx"}, "How many pixels away the cursor needs to be to make the current dragged component snap to something", 7f).setID(0);
    private final Setting<Float> lineRange = new Setting<>("LineRange", new String[]{"RectangleRange"}, "From how many pixels away to draw the lines in the center.", 93f).setID(1);

    @Override
    public void onEnable() {
        super.onEnable();
        mc.displayGuiScreen(Committee.committeeHudEditor);
        this.toggle();
    }
}

