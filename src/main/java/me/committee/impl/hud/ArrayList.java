package me.committee.impl.hud;

import me.committee.Committee;
import me.committee.api.feature.ToggleableFeature;
import me.committee.api.gui.hud.elements.HudElement;
import me.committee.api.module.Module;
import me.committee.api.setting.Colour;
import me.committee.impl.modules.client.HudOverlay;
import net.minecraft.client.gui.ScaledResolution;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayList extends HudElement {

    public ArrayList() {
        super("ArrayList", new String[]{}, "Renders a list of enabled modules.", 10, 110, 0, 0);
    }

    private float lastWidth;
    private float lastHeight;

    @Override
    public void render(float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(mc);

        final List<Module> enabledModules = Committee.moduleManager.getModules()
                .stream()
                .filter(ToggleableFeature::isEnabled)
                .filter(module -> !module.isHidden())
                .sorted(Comparator.comparingInt(module ->  mc.fontRenderer.getStringWidth(module.getName())))
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.reverse(list);
                    return list;
                }));

        if (enabledModules.isEmpty())
            return;

        final int width = enabledModules.stream().mapToInt(module -> mc.fontRenderer.getStringWidth(module.getName())).max().orElse(0);

        this.setWidth(width);
        this.setHeight(mc.fontRenderer.FONT_HEIGHT * enabledModules.size());

        if (this.getY() > sr.getScaledHeight() / 2.0f) {
            if (this.getX() > sr.getScaledWidth() / 2.0f) this.drawBottomRight(enabledModules); // Bottom Right
            else this.drawBottomLeft(enabledModules); // Bottom Left
        } else {
            if (this.getX() > sr.getScaledWidth() / 2.0f) this.drawTopRight(enabledModules); // Top Right
            else this.drawTopLeft(enabledModules); // Top Left
        }

        this.lastWidth = this.getWidth();
        this.lastHeight = this.getHeight();
    }

    private void drawBottomRight(List<Module> modules) {
        if (this.getWidth() != this.lastWidth && this.lastWidth != 0)
            this.setX(this.getX() + (this.lastWidth - this.getWidth()));

        if (this.getHeight() != this.lastHeight && this.lastHeight != 0)
            this.setY(this.getY() + (this.lastHeight - this.getHeight()));

        float currentY = this.getY();

        Collections.reverse(modules);

        for (Module enabledModule : modules) {
            final float xPos = this.getX() + this.getWidth() - mc.fontRenderer.getStringWidth(enabledModule.getName());

            final Colour colour;
            colour = (Boolean) Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(18).getValue()
                    ? (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(0).getValue()
                    : (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(17).getValue();
            mc.fontRenderer.drawString(
                    enabledModule.getName(),
                    (int)xPos,
                    (int) (currentY + mc.fontRenderer.FONT_HEIGHT / 2.0f),
                    colour.hashCode());

            currentY += mc.fontRenderer.FONT_HEIGHT;
        }
    }

    private void drawBottomLeft(List<Module> modules) {
        float currentY = this.getY();

        Collections.reverse(modules);

        for (Module enabledModule : modules) {

            final Colour colour;
            colour = (Boolean) Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(18).getValue()
                    ? (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(0).getValue()
                    : (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(17).getValue();
            mc.fontRenderer.drawString(
                    enabledModule.getName(),
                    (int) this.getX(),
                    (int) (currentY + mc.fontRenderer.FONT_HEIGHT / 2.0f),
                    colour.hashCode());

            currentY += mc.fontRenderer.FONT_HEIGHT;
        }
    }

    private void drawTopRight(List<Module> modules) {
        if (this.getWidth() != this.lastWidth && this.lastWidth != 0)
            this.setX(this.getX() + (this.lastWidth - this.getWidth()));

        float currentY = this.getY();

        for (Module enabledModule : modules) {
            final float xPos = this.getX() + this.getWidth() - mc.fontRenderer.getStringWidth(enabledModule.getName());

            final Colour colour;
            colour = (Boolean) Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(18).getValue()
                    ? (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(0).getValue()
                    : (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(17).getValue();
            mc.fontRenderer.drawString(
                    enabledModule.getName(),
                    (int)xPos,
                    (int) (currentY + mc.fontRenderer.FONT_HEIGHT / 2.0f),
                    colour.hashCode());

            currentY += mc.fontRenderer.FONT_HEIGHT;
        }
    }

    private void drawTopLeft(List<Module> modules) {
        float currentY = this.getY();

        for (Module enabledModule : modules) {

            final Colour colour;
            colour = (Boolean) Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(18).getValue()
                    ? (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(0).getValue()
                    : (Colour)Committee.moduleManager.getModuleByClass(HudOverlay.class).getSettingById(17).getValue();
            mc.fontRenderer.drawString(
                    enabledModule.getName(),
                    (int) this.getX(),
                    (int) (currentY + mc.fontRenderer.FONT_HEIGHT / 2.0f),
                    colour.hashCode());

            currentY += mc.fontRenderer.FONT_HEIGHT;
        }
    }

}
