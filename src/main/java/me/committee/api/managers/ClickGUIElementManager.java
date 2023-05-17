package me.committee.api.managers;

import me.committee.Committee;
import me.committee.api.gui.clickgui.elements.*;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.xenforu.kelo.util.font.Fonts;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class    ClickGUIElementManager {

    protected Minecraft mc = Minecraft.getMinecraft();

    private final List<CategoryComponent> elements = new ArrayList<>();

    public void init() {

        final int categoryWidth = Arrays.stream(Module.Category.values())
                .mapToInt(value -> Fonts.arialFont.getStringWidth(value.name()) + 30)
                .max()
                .orElse(100);

        int currentX = 100;
        int currentY = 100;
        int incrementX = 5;

        for (Module.Category category : Module.Category.values()) {
            final CategoryComponent categoryComponent = new CategoryComponent(
                    (float) currentX,
                    (float) currentY,
                    0.0f,
                    0.0f,
                    (float) categoryWidth,
                    (float) (mc.fontRenderer.FONT_HEIGHT * 1.5),
                    category
            );
            this.elements.add(categoryComponent);
            currentX += categoryWidth + incrementX;

            // Add module component here

            int moduleY = 0;

            for (Module module : Committee.moduleManager.getModulesByCategory(category)) {

                final ModuleComponent moduleComponent = new ModuleComponent(
                        2,
                        moduleY,
                        currentX,
                        currentY,
                        categoryWidth - 4,
                        mc.fontRenderer.FONT_HEIGHT + 4,
                        module.getName(),
                        module
                );

                categoryComponent.addChildElement(moduleComponent);

                moduleY += mc.fontRenderer.FONT_HEIGHT + 4;

                AtomicInteger settingY = new AtomicInteger();

                module.getSettings().forEach((integer, setting) -> {
                    if (setting.getValue() instanceof Boolean) {
                        moduleComponent.addChildElement(new BooleanSettingComponent(
                                0,
                                settingY.get(),
                                moduleComponent.getX(),
                                moduleComponent.getY(),
                                categoryWidth - 8,
                                mc.fontRenderer.FONT_HEIGHT + 4,
                                setting.getName(),
                                (Setting<Boolean>) setting
                        ));
                    } else if (setting.getValue() instanceof Enum<?>) {
                        moduleComponent.addChildElement(new EnumSettingComponent(
                                0,
                                settingY.get(),
                                moduleComponent.getX(),
                                moduleComponent.getY(),
                                categoryWidth - 8,
                                mc.fontRenderer.FONT_HEIGHT + 4,
                                setting.getName(),
                                (Setting<Enum<?>>) setting
                        ));
                    } else if (setting.getValue() instanceof Number) {
                        if (setting.getMin() != null && setting.getMax() != null && setting.getInc() != null) {
                            moduleComponent.addChildElement(new SliderSettingComponent(
                                    0,
                                    settingY.get(),
                                    moduleComponent.getX(),
                                    moduleComponent.getY(),
                                    categoryWidth - 8,
                                    mc.fontRenderer.FONT_HEIGHT + 4,
                                    setting.getName(),
                                    (Setting<Number>) setting
                            ));
                        } else {
                            moduleComponent.addChildElement(new UnknownSettingComponent(
                                    0,
                                    settingY.get(),
                                    moduleComponent.getX(),
                                    moduleComponent.getY(),
                                    categoryWidth - 8,
                                    mc.fontRenderer.FONT_HEIGHT + 4,
                                    setting.getName(),
                                    setting
                            ));
                        }
                    } else {
                        moduleComponent.addChildElement(new UnknownSettingComponent(
                                0,
                                settingY.get(),
                                moduleComponent.getX(),
                                moduleComponent.getY(),
                                categoryWidth - 8,
                                mc.fontRenderer.FONT_HEIGHT + 4,
                                setting.getName(),
                                setting
                        ));
                    }

                    settingY.addAndGet(mc.fontRenderer.FONT_HEIGHT + 4);
                });
            }
        }
    }

    public List<CategoryComponent> getElements() {
        return elements;
    }
}