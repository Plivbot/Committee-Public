package me.committee.api.gui.clickgui.elements;

import me.committee.api.setting.Setting;
import me.committee.api.util.RenderUtil;

import java.util.ArrayList;

public class BooleanSettingComponent extends NamedGuiComponent {

    private final Setting<Boolean> setting;

    public BooleanSettingComponent(float relX, float relY, float parentX, float parentY, float width, float height, ArrayList<GuiComponent> childElements, String name, Setting<Boolean> setting) {
        super(relX, relY, parentX, parentY, width, height, childElements, name);
        this.setting = setting;
    }

    public BooleanSettingComponent(float relX, float relY, float parentX, float parentY, float width, float height, String name, Setting<Boolean> setting) {
        super(relX, relY, parentX, parentY, width, height, name);
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks, boolean renderTooltip, boolean hover, int waitTime) {
        final boolean enabledModule = this.setting.getValue();

        if (enabledModule) {
            RenderUtil.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0xCCAC5FB5);
        } else if (hover) {
            RenderUtil.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0xCC551E5B);
        } else {
            RenderUtil.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0xCC111111);
        }

        drawContent(mouseX, mouseY, this.getName());

        super.render(mouseX, mouseY, partialTicks, renderTooltip, hover, waitTime);
    }

    @Override
    public String getDescription() {
        return setting.getDesc();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isInSquare(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight())) {
            if (mouseButton == 0) this.setting.setValue(!this.setting.getValue());
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public Setting<Boolean> getSetting() {
        return setting;
    }
}
