package me.committee.api.gui.hud;

import me.committee.Committee;
import me.committee.api.setting.Setting;
import me.committee.api.util.RenderUtil;
import me.committee.api.util.ThemedRenderUtil;
import me.committee.impl.modules.client.HudEditor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;

public class CommitteeHudEditor extends GuiScreen {

    private int lastMouseX = 0;
    private int lastMouseY = 0;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        final int xDelta = mouseX - this.lastMouseX;
        final int yDelta = mouseY - this.lastMouseY;

        Committee.hudElementManager.getElements()
                .forEach(hudElement -> {
                    if (hudElement.isDragging()) {
                        hudElement.setXNoOffscreen(hudElement.getX() + xDelta);
                        hudElement.setYNoOffscreen(hudElement.getY() + yDelta);
                    }
                    hudElement.render(partialTicks);
                });

        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;

        final ScaledResolution sr = new ScaledResolution(mc);
        final float scaledWidth = sr.getScaledWidth();
        final float scaledHeight = sr.getScaledHeight();

        final float snapRange = ((Setting<Float>) Committee.moduleManager.getModuleByClass(HudEditor.class).getSettingById(0)).getValue();

        final float xMinSnap = (scaledWidth / 2.0f) - snapRange;
        final float xMaxSnap = (scaledWidth / 2.0f) + snapRange;
        final float yMinSnap = (scaledHeight / 2.0f) - snapRange;
        final float yMaxSnap = (scaledHeight / 2.0f) + snapRange;

        final float lineRange = ((Setting<Float>) Committee.moduleManager.getModuleByClass(HudEditor.class).getSettingById(1)).getValue();

        if (mouseX >= xMinSnap - lineRange && mouseX <= xMaxSnap + lineRange) {
            if (mouseX >= xMinSnap && mouseX <= xMaxSnap) {
                RenderUtil.drawRect(xMinSnap, 0, 1, scaledHeight, 0, 255, 0, 255);
                RenderUtil.drawRect(xMaxSnap - 1, 0, 1, scaledHeight, 0, 255, 0, 255);
            } else {
                RenderUtil.drawRect(xMinSnap, 0, 1, scaledHeight, 255, 0, 0, 255);
                RenderUtil.drawRect(xMaxSnap - 1, 0, 1, scaledHeight, 255, 0, 0, 255);
            }
        }

        if (mouseY >= yMinSnap - lineRange && mouseY <= yMaxSnap + lineRange) {
            if (mouseY >= yMinSnap && mouseY <= yMaxSnap) {
                RenderUtil.drawRect(0, yMinSnap, scaledWidth, 1,0, 255, 0, 255);
                RenderUtil.drawRect(0, yMaxSnap - 1, scaledWidth, 1, 0, 255, 0, 255);
            } else {
                RenderUtil.drawRect(0, yMinSnap, scaledWidth, 1,255, 0, 0, 255);
                RenderUtil.drawRect(0, yMaxSnap - 1, scaledWidth, 1, 255, 0, 0, 255);
            }
        }


    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        Committee.hudElementManager.getElements()
                .stream()
                .filter(hudElement ->
                        RenderUtil.isInSquare(mouseX, mouseY, hudElement.getX(), hudElement.getY(), hudElement.getWidth(), hudElement.getHeight())
                ).forEach(hudElement -> {
                    if (mouseButton == 0) hudElement.setDragging(true);
                    else if (mouseButton == 1) hudElement.toggle();
                });
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);

        final ScaledResolution sr = new ScaledResolution(mc);
        final float snapRange = ((Setting<Float>) Committee.moduleManager.getSetting(HudEditor.class, "SnapPixels")).getValue();

        final float xMinSnap = (sr.getScaledWidth() / 2.0f) - snapRange;
        final float xMaxSnap = (sr.getScaledWidth() / 2.0f) + snapRange;
        final float yMinSnap = (sr.getScaledHeight() / 2.0f) - snapRange;
        final float yMaxSnap = (sr.getScaledHeight() / 2.0f) + snapRange;

        Committee.hudElementManager.getElements().forEach(hudElement -> {
            if (hudElement.isDragging()) {
                if (mouseX >= xMinSnap && mouseX <= xMaxSnap)
                    hudElement.setX(sr.getScaledWidth() / 2.0f - (hudElement.getWidth() / 2));

                if (mouseY >= yMinSnap && mouseY <= yMaxSnap)
                    hudElement.setY(sr.getScaledHeight() / 2.0f - (hudElement.getHeight() / 2));
            }
            hudElement.setDragging(false);
        });
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
