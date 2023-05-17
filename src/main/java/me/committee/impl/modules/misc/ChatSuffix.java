package me.committee.impl.modules.misc;

import com.google.common.collect.ImmutableSet;
import me.committee.api.command.Command;
import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.mixin.mixins.accessors.AccessorCPacketChatMessage;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.network.SendPacketEvent;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.Set;

public class ChatSuffix extends Module {

    private final Setting<Mode> mode = new Setting<>("Mode", new String[]{"M"}, "Sets what chat append mode to use.", Mode.DEFAULT);
    private final Setting<Boolean> blueChat = new Setting<>("Blue", new String[]{"BlueSuffix"}, "Turns the suffix blue on 9b9t", false);
    private final Setting<Boolean> prefixBlock = new Setting<>("PrefixBlock", new String[]{"BlockPrefix"}, "Stops chat append on common prefix.", true);

    final Set<Character> commonPrefix = ImmutableSet.of('.', ',', '#', '~', '@', '&', '!', '`', '\\', '+', '=', '-');

    public ChatSuffix() {
        super("ChatSuffix", new String[]{"ChatAppend", "Suffix"}, "Adds a suffix to the end of your chat messages.", Category.MISC);
    }

    @EventSubscribe
    public void onChatPacket(SendPacketEvent event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            final CPacketChatMessage packet = ((CPacketChatMessage) event.getPacket());
            final String message = packet.getMessage();
            final String suffix = mode.getValue().suffix;

            if (
                    message.length() + ((this.blueChat.getValue() ? " `" : " ") + suffix).length() > 255 ||
                            message.startsWith(Command.PREFIX) ||
                            message.startsWith("/") ||
                            (prefixBlock.getValue() && commonPrefix.contains(message.charAt(0)))
            ) {
                return;
            }

            ((AccessorCPacketChatMessage) packet).setMessage(packet.getMessage() + (this.blueChat.getValue() ? " `" : " ") + suffix);
        }
    }

    public enum Mode {
        DEFAULT("\u23d0 \u1d04\u1d0f\u1d0d\u1d0d\u026a\u1d1b\u1d1b\u1d07\u1d07"),
        C1("> Committee <"),
        C2("\u23d0 \u0020\u1d9c\u1d52\u1d50\u1d50\u1Da6\u1d57\u1d57\u1d49\u1d49"),
        C3("\u23d0 \u01dd\u01dd\u0287\u0287\u0131\u026f\u026f\u006f\u0186"),
        CM("\u23d0 Committee Member"),
        BALLS("\u23d0 \u0299\u1d00\u029f\u029f\ua731"),
        SPICY("\u23d0 \u4e02\u5369\u4e28\u4e02\u311a");

        final String suffix;

        Mode(String suffix) {
            this.suffix = suffix;
        }
    }

}
