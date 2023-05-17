package me.committee.impl.event.events.render;

import me.committee.api.eventsystem.event.Event;
import me.committee.api.setting.Colour;

public class FishLineColourEvent extends Event {

    private Colour colour;

    public FishLineColourEvent(Colour colour) {
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }


}
