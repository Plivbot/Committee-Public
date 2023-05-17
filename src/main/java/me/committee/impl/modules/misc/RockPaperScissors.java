package me.committee.impl.modules.misc;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.util.MessageSendHelper;
import me.committee.impl.event.events.chat.ChatEvent;

import java.util.Objects;

public class RockPaperScissors extends Module {


    public RockPaperScissors() {super ("rock", new String[]{"Name", "NH"}, "Highlights ur name in chat", Category.MISC); }

    private final String[] rpc = {"rock", "paper", "scissors"};

    @EventSubscribe
    public void onMessage (ChatEvent event) {
        if (Objects.equals(event.getMessage(), "rock")) {
            MessageSendHelper.sendMessage("Balls");
        }
    }
}