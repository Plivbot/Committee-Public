package me.committee.impl.event.events.render;

import me.committee.api.eventsystem.event.Event;

public class TabOverlayPlayerEvent extends Event {

    private String name;

    public TabOverlayPlayerEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
