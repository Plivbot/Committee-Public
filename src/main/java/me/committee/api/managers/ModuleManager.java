package me.committee.api.managers;

import me.committee.Committee;
import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.input.KeyboardEvent;
import org.lwjgl.input.Keyboard;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModuleManager {

    private final ArrayList<Module> modules = new ArrayList<>();

    public static void loadSettings(Module module) throws IllegalAccessException {

        final List<Integer> usedIds = new ArrayList<>();
        final List<Setting<?>> settings = new ArrayList<>();

        for (Field declaredField : module.getClass().getDeclaredFields()) {
            if (Setting.class.isAssignableFrom(declaredField.getType())) {
                if (!declaredField.isAccessible()) {
                    declaredField.setAccessible(true);
                }

                final Setting<?> setting = (Setting<?>) declaredField.get(module);
                setting.setOwner(module.getClass());

                settings.add(setting);
                if (setting.getId() != Integer.MAX_VALUE) {
                    if (usedIds.contains(setting.getId())) {
                        throw new IllegalStateException("Two settings with the same id in class " + module.getClass().getCanonicalName());
                    }

                    usedIds.add(setting.getId());
                }
            }
        }

        int currentId = 0;

        for (Setting<?> setting : settings) {


            if (setting.getId() == Integer.MAX_VALUE) {
                while (usedIds.contains(currentId)) {
                    currentId++;
                }

                module.getSettings().put(currentId, setting);
                currentId++;
            } else {
                module.getSettings().put(setting.getId(), setting);
            }
        }
    }

    public void init() {
        Committee.LOGGER.info("Loading modules...");

        Set<Class<? extends Module>> classes = new Reflections("me.committee.impl.modules").getSubTypesOf(Module.class);

        for (Class<?> clazz : classes) {
            try {
                Committee.LOGGER.debug("Adding: " + clazz.getCanonicalName());

                final Module module = (Module) clazz.newInstance();
                loadSettings(module);

                this.modules.add(module);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Committee.LOGGER.info("Modules initialized.");
    }

    @EventSubscribe
    public void onKeyboardInput(KeyboardEvent event) {
        if (Keyboard.getEventKeyState() && !Keyboard.isRepeatEvent()) {
            final int eventKey = Keyboard.getEventKey();
            if (eventKey == Keyboard.KEY_NONE) return;

            for (Module module : this.modules) {
                if (module.getKeyBind() == eventKey)
                    module.toggle();
            }
        }
    }

    public Module getModuleByClass(Class<? extends Module> clazz) {
        for (Module module : this.modules) {
            if (module.getClass() == clazz)
                return module;
        }
        return null;
    }

    public Module getModuleByName(String name) {
        return getModuleByName(name, true);
    }

    public Module getModuleByName(String name, boolean ignorecase) {
        // todo: remove duped code
        for (Module module : modules) {
            if (ignorecase) {
                if (module.getName().equalsIgnoreCase(name)) {
                    return module;
                }
                for (String alias : module.getAlias()) {
                    if (alias.equalsIgnoreCase(name)) {
                        return module;
                    }
                }
            } else {
                if (module.getName().equals(name)) {
                    return module;
                }
                for (String alias : module.getAlias()) {
                    if (alias.equals(name)) {
                        return module;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Convenience method to get settings more easily with less code
     *
     * Deprecated - use ids instead, getting the setting from a hashmap with an id is faster than comparing strings
     * @param clazz the module class to search from
     * @param settingName the setting's name we want to find
     * @return the setting object
     */
    @Deprecated
    public Setting<?> getSetting(Class<? extends Module> clazz, String settingName) {
        return getModuleByClass(clazz).getSettingByName(settingName);
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public List<Module> getModulesByCategory(Module.Category category) {
        return this.modules.stream().filter(module -> module.getCategory() == category).collect(Collectors.toList());
    }
}
