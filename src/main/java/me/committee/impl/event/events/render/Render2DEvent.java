package me.committee.impl.event.events.render;

import me.committee.api.eventsystem.event.Event;

public class Render2DEvent extends Event {

    private final float partialTicks;

    public Render2DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
