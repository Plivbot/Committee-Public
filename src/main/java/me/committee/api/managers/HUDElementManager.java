package me.committee.api.managers;

import me.committee.Committee;
import me.committee.api.gui.hud.elements.HudElement;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HUDElementManager {

    private final List<HudElement> elements = new ArrayList<>();

    public void init() {
        Committee.LOGGER.info("Loading HUD Elements...");

        Set<Class<? extends HudElement>> classes = new Reflections("me.committee.impl.hud").getSubTypesOf(HudElement.class);

        for (Class<?> clazz : classes) {
            try {
                Committee.LOGGER.debug("Adding: " + clazz.getCanonicalName());

                final HudElement hudElement = (HudElement) clazz.newInstance();
                Committee.EVENT_BUS.subscribe(hudElement);

                this.elements.add(hudElement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Committee.LOGGER.info("Hud Elements initialized.");
    }

    public List<HudElement> getElements() {
        return elements;
    }
}