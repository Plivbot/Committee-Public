package me.committee.impl.event.events.render;

import me.committee.api.eventsystem.event.Event;

public class RenderLavaFogEvent extends Event {

    float density;

    public RenderLavaFogEvent(float density){
        this.density = density;
    }

    public void setDensity(float density){
        this.density = density;
    }

    public float getDensity() {return density;}
}
