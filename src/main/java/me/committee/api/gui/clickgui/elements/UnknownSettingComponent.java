package me.committee.api.gui.clickgui.elements;

import me.committee.api.setting.Setting;
import me.committee.api.util.RenderUtil;

import java.util.ArrayList;

public class UnknownSettingComponent extends NamedGuiComponent {

    private final Setting<?> setting;

    public UnknownSettingComponent(float relX, float relY, float parentX, float parentY, float width, float height, ArrayList<GuiComponent> childElements, String name, Setting<?> setting) {
        super(relX, relY, parentX, parentY, width, height, childElements, name);
        this.setting = setting;
    }

    public UnknownSettingComponent(float relX, float relY, float parentX, float parentY, float width, float height, String name, Setting<?> setting) {
        super(relX, relY, parentX, parentY, width, height, name);
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks, boolean renderTooltip, boolean hover, int waitTime) {
        if (hover) RenderUtil.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0xCC551E5B);
        else RenderUtil.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0xCC111111);

        drawContent(mouseX, mouseY, this.getName() + " : " + this.getSetting().getValue());

        super.render(mouseX, mouseY, partialTicks, renderTooltip, hover, waitTime);
    }

    @Override
    public String getDescription() {
        return setting.getDesc();
    }

    public Setting<?> getSetting() {
        return setting;
    }
}
