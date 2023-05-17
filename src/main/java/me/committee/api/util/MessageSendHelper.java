package me.committee.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class MessageSendHelper {

    public static void sendMessage(String message, Level level, PrefixType prefixType) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString((prefixType.name + " ") + "\u00a7" + level.colorcode + message));
    }

    public static void sendMessage(String message, Level level) {
        sendMessage(message, level, PrefixType.NORMAL);
    }

    public static void sendMessage(String message, PrefixType prefixType) {
        sendMessage(message, Level.INFO, prefixType);
    }

    public static void sendMessage(String message) {
        sendMessage(message, Level.INFO, PrefixType.NORMAL);
    }

    public enum Level {
        SUCCESS("a"),
        INFO("f"),
        WARN("c"),
        DEBUG("3");

        final String colorcode;

        Level(String colorcode) {
            this.colorcode = colorcode;
        }
    }

    public enum PrefixType {
        NORMAL("\u00a7l\u00a74[\u00a76Committee\u00a74]\u00a7r"),
        IRC("\u00a7l\u00a74[\u00a76IRC\u00a74]\u00a7r"),
        NONE("");

        final String name;

        PrefixType(String name) {
            this.name = name;
        }
    }
}
