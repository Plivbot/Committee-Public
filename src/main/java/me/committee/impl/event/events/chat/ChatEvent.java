package me.committee.impl.event.events.chat;

import com.google.common.base.Strings;
import me.committee.api.eventsystem.event.Event;

public class ChatEvent extends Event { // I pasted this from the forge event

    private final String originalMessage;
    private String message;

    public ChatEvent(String message) {
        this.setMessage(message);
        this.originalMessage = Strings.nullToEmpty(message);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = Strings.nullToEmpty(message);
    }

    public String getOriginalMessage() {
        return this.originalMessage;
    }
}
