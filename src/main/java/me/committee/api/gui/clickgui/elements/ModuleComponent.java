package me.committee.api.gui.clickgui.elements;

import me.committee.api.module.Module;
import me.committee.api.util.RenderUtil;

import java.util.ArrayList;

public class ModuleComponent extends NamedGuiComponent {

    private final Module module;

    public ModuleComponent(float relX, float relY, float parentX, float parentY, float width, float height, ArrayList<GuiComponent> childElements, String name, Module module) {
        super(relX, relY, parentX, parentY, width, height, childElements, name);
        this.module = module;
        this.setOpen(false);
    }

    public ModuleComponent(float relX, float relY, float parentX, float parentY, float width, float height, String name, Module module) {
        super(relX, relY, parentX, parentY, width, height, name);
        this.module = module;
        this.setOpen(false);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks, boolean renderTooltip, boolean hover, int waitTime) {
        final boolean enabledModule = this.module.isEnabled();

        if (enabledModule) RenderUtil.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0xCCAB3CB7);
        else if (hover) RenderUtil.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0xCC551E5B);
        else RenderUtil.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0xCC000000);

        drawContent(mouseX, mouseY, this.getName());

        super.render(mouseX, mouseY, partialTicks, renderTooltip, hover, waitTime);
    }

    @Override
    public String getDescription() {
        return module.getDesc();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isInSquare(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight())) {
            if (mouseButton == 0) this.module.toggle();
            else if (mouseButton == 1) setOpen(!isOpen());
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public Module getModule() {
        return module;
    }
}
