package me.committee.impl.event.events.timing;

import me.committee.api.eventsystem.event.EventStage;

public class ClientTickEvent extends EventStage {
    public ClientTickEvent(Stage stage) {
        super(stage);
    }
}
