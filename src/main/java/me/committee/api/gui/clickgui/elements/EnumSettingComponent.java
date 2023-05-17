package me.committee.api.gui.clickgui.elements;

import me.committee.api.setting.Setting;
import me.committee.api.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;

public class EnumSettingComponent extends NamedGuiComponent {

    private final Setting<Enum<?>> setting;

    public EnumSettingComponent(float relX, float relY, float parentX, float parentY, float width, float height, ArrayList<GuiComponent> childElements, String name, Setting<Enum<?>> setting) {
        super(relX, relY, parentX, parentY, width, height, childElements, name);
        this.setting = setting;
    }

    public EnumSettingComponent(float relX, float relY, float parentX, float parentY, float width, float height, String name, Setting<Enum<?>> setting) {
        super(relX, relY, parentX, parentY, width, height, name);
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks, boolean renderTooltip, boolean hover, int waitTime) {

        if (hover) RenderUtil.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0xCC551E5B);
        else RenderUtil.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0xCC111111);

        drawContent(mouseX, mouseY, this.getName() + " : " + this.getSetting().getValue().name());

        super.render(mouseX, mouseY, partialTicks, renderTooltip, hover, waitTime);
    }

    @Override
    public String getDescription() {
        return setting.getDesc();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isInSquare(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight())) {
            final List<Enum<?>> enumValues = this.setting.getPossibleEnumValues();
            final int currentIndex = enumValues.indexOf(this.setting.getValue());
            if (mouseButton == 0)
                this.setting.setValue(enumValues.get((currentIndex + 1) % enumValues.size()));
            else if (mouseButton == 1)
                this.setting.setValue(enumValues.get(((currentIndex - 1) + (Math.abs(currentIndex - 1) * enumValues.size())) % enumValues.size()));

        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public Setting<Enum<?>> getSetting() {
        return setting;
    }
}
