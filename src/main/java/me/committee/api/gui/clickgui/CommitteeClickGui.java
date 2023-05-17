package me.committee.api.gui.clickgui;

import me.committee.Committee;
import me.committee.api.util.RenderUtil;
import me.committee.impl.modules.client.ClickGUI;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class CommitteeClickGui extends GuiScreen {

    private int lastMouseX = 0;
    private int lastMouseY = 0;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        final int xDelta = mouseX - this.lastMouseX;
        final int yDelta = mouseY - this.lastMouseY;

        boolean renderTooltip = (Boolean) Committee.moduleManager.getModuleByClass(ClickGUI.class).getSettingById(1).getValue();
        int waitTime = (Integer) Committee.moduleManager.getModuleByClass(ClickGUI.class).getSettingById(2).getValue();

        Committee.clickGuiElementManager.getElements().forEach(guiElement -> {
            if (guiElement.isDragging()) {
                guiElement.setRelX(guiElement.getX() + xDelta);
                guiElement.setRelY(guiElement.getY() + yDelta);
            }

            guiElement.render(mouseX, mouseY, partialTicks, renderTooltip, RenderUtil.isInSquare(mouseX, mouseY, guiElement.getX(), guiElement.getY(), guiElement.getWidth(), guiElement.getHeight()), waitTime);
        });

        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        Committee.clickGuiElementManager.getElements().forEach(guiElement -> guiElement.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        Committee.clickGuiElementManager.getElements().forEach(guiElement -> guiElement.setDragging(false));
        Committee.clickGuiElementManager.getElements().forEach(guiElement -> guiElement.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
