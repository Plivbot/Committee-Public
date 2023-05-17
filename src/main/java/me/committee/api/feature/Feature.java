package me.committee.api.feature;

import net.minecraft.client.Minecraft;

public abstract class Feature {

    private final String name;
    private final String[] alias;
    private final String desc;
    protected Minecraft mc = Minecraft.getMinecraft();

    public Feature(String name, String[] alias, String desc) {
        this.name = name;
        this.alias = alias;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String[] getAlias() {
        return alias;
    }

    public String getDesc() {
        return desc;
    }

}
