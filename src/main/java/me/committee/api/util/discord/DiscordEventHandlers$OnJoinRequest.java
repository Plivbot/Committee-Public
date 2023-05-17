
package me.committee.api.util.discord;

import com.sun.jna.Callback;

public interface DiscordEventHandlers$OnJoinRequest extends Callback {
    void accept(DiscordUser var1);
}
