package me.committee.impl.event.events.render;

import me.committee.api.eventsystem.event.Event;

public class SpawnParticleEvent extends Event {

    private final int particleID;

    public SpawnParticleEvent(int particleID) {
        this.particleID = particleID;
    }

    public int getParticleID() {
        return particleID;
    }

}
