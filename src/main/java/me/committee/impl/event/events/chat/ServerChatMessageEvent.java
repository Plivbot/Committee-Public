package me.committee.impl.event.events.chat;

import me.committee.api.eventsystem.event.Event;
import net.minecraft.util.text.ITextComponent;

public class ServerChatMessageEvent extends Event {
    private ITextComponent textComponent;

    public ServerChatMessageEvent(ITextComponent textComponent) {
        this.textComponent = textComponent;
    }

    public ITextComponent getTextComponent() {
        return textComponent;
    }

    public void setTextComponent(ITextComponent textComponent) {
        this.textComponent = textComponent;
    }
}
