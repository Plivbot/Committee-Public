package me.committee.impl.modules.misc;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.impl.event.events.chat.ServerChatMessageEvent;
import net.minecraft.util.text.ITextComponent;


public class NameHighlight extends Module{


    public NameHighlight() {super ("NameHighlight", new String[]{"Name", "NH"}, "Highlights ur name in chat", Category.MISC); }

    @EventSubscribe
    public void onServerChatMessage (ServerChatMessageEvent event) {
        final ITextComponent  text = event.getTextComponent(); //"idk what this is not what I need tho" -TBM (the retard)
        if (text.getUnformattedText().contains(mc.player.getName())) {
            final ITextComponent newText = ITextComponent.Serializer.jsonToComponent(ITextComponent.Serializer.componentToJson(event.getTextComponent()).replace(mc.player.getName(), "\u00A7d\u00A7l" + mc.player.getName() + "\u00A7r"));
            event.setTextComponent(newText);
            event.cancel();
        }
    }
}

