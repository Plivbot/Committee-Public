
package me.committee.api.util.discord;

import com.sun.jna.Callback;

public interface DiscordEventHandlers$OnReady extends Callback {
    void accept(DiscordUser var1);
}
