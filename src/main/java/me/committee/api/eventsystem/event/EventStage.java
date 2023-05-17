package me.committee.api.eventsystem.event;

public abstract class EventStage extends Event {

    private final Stage stage;

    public EventStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public enum Stage {
        PRE, POST
    }
}
