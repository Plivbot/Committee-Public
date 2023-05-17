package me.committee.impl.event;

import me.committee.Committee;
import me.committee.api.eventsystem.event.EventStage;
import me.committee.impl.event.events.chat.ChatEvent;
import me.committee.impl.event.events.input.KeyboardEvent;
import me.committee.impl.event.events.timing.ClientTickEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ForgeEventProcessor {

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {

        ChatEvent chatEvent = new ChatEvent(event.getMessage());
        Committee.EVENT_BUS.post(chatEvent);

        if (chatEvent.isCancelled()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onKeyboardInput(InputEvent.KeyInputEvent event) {
        KeyboardEvent keyboardEvent = new KeyboardEvent();
        Committee.EVENT_BUS.post(keyboardEvent);

        if (keyboardEvent.isCancelled()) event.setCanceled(true);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.PlayerTickEvent forgeEvent) {
        ClientTickEvent event;

        if (forgeEvent.phase == TickEvent.Phase.START) {
            event = new ClientTickEvent(EventStage.Stage.PRE);
        } else {
            event = new ClientTickEvent(EventStage.Stage.POST);
        }

        Committee.EVENT_BUS.post(event);

        if (event.isCancelled()) {
            forgeEvent.setCanceled(true);
        }
    }
}
