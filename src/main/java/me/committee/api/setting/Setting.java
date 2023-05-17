package me.committee.api.setting;

import me.committee.Committee;
import me.committee.api.util.MathUtil;
import me.committee.impl.event.events.input.SettingChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class Setting<T> {

    private final String name;
    private final String[] alias;
    private final String desc;
    private Class owner;
    private final T min;
    private final T max;
    private final T inc;
    private T value;
    private int id = Integer.MAX_VALUE;

    public Setting(String name, String[] alias, String desc, T value) {
        this.name = name;
        this.alias = alias;
        this.desc = desc;
        this.value = value;
        this.min = null;
        this.max = null;
        this.inc = null;
    }

    public Setting(String name, String[] alias, String desc, T value, T min, T max, T inc) {
        this.name = name;
        this.alias = alias;
        this.desc = desc;
        this.value = value;
        this.min = min;
        this.max = max;
        this.inc = inc;
    }

    public Setting(String name, String desc, T value) {
        this.name = name;
        this.alias = new String[]{};
        this.desc = desc;
        this.value = value;
        this.min = null;
        this.max = null;
        this.inc = null;
    }

    public Setting(String name, String desc, T value, T min, T max, T inc) {
        this.name = name;
        this.desc = desc;
        this.alias = new String[]{};
        this.value = value;
        this.min = min;
        this.max = max;
        this.inc = inc;
    }

    public Setting<T> setID(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public String[] getAlias() {
        return this.alias;
    }

    public String getDesc() {
        return desc;
    }

    public T getValue() {
        return this.value;
    }

    @SuppressWarnings("unchecked")
    public void setValue(T value) {
        final SettingChangeEvent<T> event = new SettingChangeEvent<>(this, value);

        if (this.min == null || this.max == null) {
            this.value = value;
        } else if (this.value instanceof Number) {
            this.value = (T) MathUtil.clamp((Number) value, (Number) this.min, (Number) this.max);
        }

        Committee.EVENT_BUS.post(event);

        if (event.isCancelled()) {
            this.value = event.getOldValue();
        }
    }

    /**
     * Sets the value of the setting's enum
     *
     * @param value the name of the enum we want to be set
     * @return did we find an enum with that name
     */
    @SuppressWarnings("unchecked")
    public boolean setValueOfEnum(String value) {
        if (this.getValue() instanceof Enum) {
            for (Enum<?> enumConstant : ((Enum<?>) this.getValue()).getClass().getEnumConstants()) {
                if (enumConstant.name().equalsIgnoreCase(value)) {
                    this.value = (T) enumConstant;
                    return true;
                }
            }
            this.value = (T) this.getValue().getClass().getEnumConstants()[0];
        }
        return false;
    }

    public List<Enum<?>> getPossibleEnumValues() {
        final List<Enum<?>> enums = new ArrayList<>();
        if (this.getValue() instanceof Enum) {
            for (Enum<?> enumConstant : ((Enum<?>) this.getValue()).getClass().getEnumConstants()) {
                enums.add(enumConstant);
            }
        }
        return enums;
    }

    public void setOwner(Class owner) {
        this.owner = owner;
    }

    public Class getOwner() {
        return owner;
    }

    public T getMin() {
        return this.min;
    }

    public T getMax() {
        return this.max;
    }

    public T getInc() {
        return this.inc;
    }

    public int getId() {
        return id;
    }
}
