package me.committee.impl.event.events.render;

import me.committee.api.eventsystem.event.Event;
import net.minecraft.entity.Entity;

public class TeamColourEvent extends Event {

    private final Entity entity;
    private int colour;

    public TeamColourEvent(Entity entity, int colour) {
        this.entity = entity;
        this.colour = colour;
    }

    public Entity getEntity() {
        return entity;
    }

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

}
