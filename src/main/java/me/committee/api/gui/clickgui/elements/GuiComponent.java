package me.committee.api.gui.clickgui.elements;

import me.committee.api.util.RenderUtil;
import me.committee.api.util.StopWatch;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public abstract class GuiComponent {

    private final float width;
    private final float height;
    private final ArrayList<GuiComponent> childElements;
    /**
     * For how long has the cursor been on top of the button in ms
     */
    public StopWatch hoverTimer = null;
    protected Minecraft mc = Minecraft.getMinecraft();
    private float relX;
    private float relY;
    private float parentX;
    private float parentY;
    private boolean open;
    private boolean showTooltip;

    protected GuiComponent(float relX, float relY, float parentX, float parentY, float width, float height, ArrayList<GuiComponent> childElements) {
        this.relX = relX;
        this.relY = relY;
        this.parentX = parentX;
        this.parentY = parentY;
        this.width = width;
        this.height = height;
        this.childElements = childElements;
        this.open = true;
    }

    protected GuiComponent(float relX, float relY, float parentX, float parentY, float width, float height) {
        this.relX = relX;
        this.relY = relY;
        this.parentX = parentX;
        this.parentY = parentY;
        this.width = width;
        this.height = height;
        this.childElements = new ArrayList<>();
        this.open = true;
    }

    public void render(int mouseX, int mouseY, float partialTicks, boolean renderTooltip, boolean hover, int waitTime) {

        if (hover && hoverTimer == null) {
            hoverTimer = new StopWatch();
        } else if (!hover) {
            hoverTimer = null;
        }

        showTooltip = hoverTimer != null && hoverTimer.hasPassed(waitTime);
        if (this.isOpen()) {
            this.getChildElements().forEach(child -> {
                child.setParentX(getX());
                child.setParentY(getY() + this.getHeight());

                child.render(
                        mouseX,
                        mouseY,
                        partialTicks,
                        renderTooltip,
                        RenderUtil.isInSquare(mouseX, mouseY, child.getX(), child.getY(), child.getWidth(), child.getHeight()),
                        waitTime
                );
            });
        }

        final String desc = getDescription();

        if (showTooltip && renderTooltip && desc != null) {

            // https://www.reddit.com/r/opengl/comments/bblwf8/how_to_force_objects_that_are_actually_behind/
            glPushMatrix();

            glDepthFunc(GL_ALWAYS);

            glTranslated(0, 0, 1);
            glEnable(GL_BLEND);
            renderTooltipWithContent(mouseX + 10, mouseY + 10, desc);
            glDisable(GL_BLEND);
            glTranslated(0, 0, -1);
            glDepthFunc(GL_LEQUAL);
            glPopMatrix();
        }
    }

    /**
     * @return null if there is no desc
     */
    public abstract String getDescription();

    public final void renderTooltipWithContent(int posX, int posY, String content) {
        RenderUtil.drawRect(posX + 5, posY + 5, mc.fontRenderer.getStringWidth(content) + 2, mc.fontRenderer.FONT_HEIGHT + 2, 0xaa000000);
        mc.fontRenderer.drawString(content, posX + 5 + 1 /* 1 is to keep centered in the box in both*/, posY + 5 + 1, 0xffffff);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isOpen()) {
            this.getChildElements().forEach(child -> child.mouseClicked(mouseX, mouseY, mouseButton));
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.isOpen()) {
            this.getChildElements().forEach(child -> child.mouseReleased(mouseX, mouseY, state));
        }
    }

    public float getX() {
        return parentX + relX;
    }

    public float getY() {
        return parentY + relY;
    }

    public float getRelX() {
        return relX;
    }

    public void setRelX(float relX) {
        this.relX = relX;
    }

    public float getRelY() {
        return relY;
    }

    public void setRelY(float relY) {
        this.relY = relY;
    }

    public float getParentX() {
        return parentX;
    }

    public void setParentX(float parentX) {
        this.parentX = parentX;
    }

    public float getParentY() {
        return parentY;
    }

    public void setParentY(float parentY) {
        this.parentY = parentY;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public ArrayList<GuiComponent> getChildElements() {
        return childElements;
    }

    public void addChildElement(GuiComponent element) {
        this.childElements.add(element);
    }

    public float getAdditionalOffset() {
        if (!this.isOpen()) return 0.0f;
        float offset = 0;
        for (GuiComponent childElement : this.childElements)
            offset += childElement.getHeight();
        return offset;
    }

}
