package me.committee.impl.event.events.render;

import me.committee.api.eventsystem.event.Event;
import net.minecraft.entity.Entity;

public class OutlineActiveEvent extends Event {

    private final Entity entity;

    public OutlineActiveEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

}
