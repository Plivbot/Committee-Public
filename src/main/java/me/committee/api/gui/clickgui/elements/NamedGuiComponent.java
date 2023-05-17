package me.committee.api.gui.clickgui.elements;

import me.committee.Committee;
import me.committee.api.util.RenderUtil;
import me.committee.impl.modules.client.ClickGUI;

import java.awt.*;
import java.util.ArrayList;

public abstract class NamedGuiComponent extends GuiComponent {

    private final String name;

    public NamedGuiComponent(float relX, float relY, float parentX, float parentY, float width, float height, ArrayList<GuiComponent> childElements, String name) {
        super(relX, relY, parentX, parentY, width, height, childElements);
        this.name = name;
    }

    public NamedGuiComponent(float relX, float relY, float parentX, float parentY, float width, float height, String name) {
        super(relX, relY, parentX, parentY, width, height);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void drawContent(int mouseX, int mouseY, String content) {
        mc.fontRenderer.drawString(
                (Boolean) Committee.moduleManager.getModuleByClass(ClickGUI.class).getSettingById(3).getValue() ?
                        RenderUtil.getTextWithCutoff(content, mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight()) :
                        content,
                (int) (this.getX() + 2),
                (int) (this.getY() + (this.getHeight() / 2) - (mc.fontRenderer.FONT_HEIGHT / 2)),
                Color.WHITE.hashCode()
        );
    }
}
