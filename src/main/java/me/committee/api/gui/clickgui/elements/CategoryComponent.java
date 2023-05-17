package me.committee.api.gui.clickgui.elements;

import me.committee.api.module.Module;
import me.committee.api.util.RenderUtil;

import java.util.ArrayList;

public class CategoryComponent extends NamedGuiComponent {

    private final Module.Category category;
    private boolean dragging = false;

    public CategoryComponent(float relX, float relY, float parentX, float parentY, float width, float height, ArrayList<GuiComponent> childElements, Module.Category category) {
        super(relX, relY, parentX, parentY, width, height, childElements, category.getReadableName());
        this.category = category;
    }

    public CategoryComponent(float relX, float relY, float parentX, float parentY, float width, float height, Module.Category category) {
        super(relX, relY, parentX, parentY, width, height, category.getReadableName());
        this.category = category;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks, boolean renderTooltip, boolean hover, int waitTime) {
        RenderUtil.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0xDDFF44FF);

        drawContent(mouseX, mouseY, this.getName());

        if (this.isOpen()) {
            int nextOffset = 0;

            for (GuiComponent child : this.getChildElements()) {
                child.setParentX(this.getX());
                child.setParentY(this.getY() + this.getHeight() + nextOffset);
                child.render(mouseX, mouseY, partialTicks, renderTooltip, RenderUtil.isInSquare(mouseX, mouseY, child.getX(), child.getY(), child.getWidth(), child.getHeight()), waitTime);
                nextOffset += child.getAdditionalOffset();
            }
        }
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isInSquare(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight())) {
            if (mouseButton == 0) this.setDragging(true);
            else if (mouseButton == 1) setOpen(!isOpen());
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }


    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

}
