

package me.committee.api.util.discord;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DiscordEventHandlers extends Structure {
    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList("ready", "disconnected", "errored", "joinGame", "spectateGame", "joinRequest"));
    public me.committee.api.util.discord.DiscordEventHandlers$OnReady ready;
    public me.committee.api.util.discord.DiscordEventHandlers$OnStatus disconnected;
    public me.committee.api.util.discord.DiscordEventHandlers$OnStatus errored;
    public me.committee.api.util.discord.DiscordEventHandlers$OnGameUpdate joinGame;
    public me.committee.api.util.discord.DiscordEventHandlers$OnGameUpdate spectateGame;
    public me.committee.api.util.discord.DiscordEventHandlers$OnJoinRequest joinRequest;

    public DiscordEventHandlers() {
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof DiscordEventHandlers)) {
            return false;
        } else {
            DiscordEventHandlers that = (DiscordEventHandlers)o;
            return Objects.equals(this.ready, that.ready) && Objects.equals(this.disconnected, that.disconnected) && Objects.equals(this.errored, that.errored) && Objects.equals(this.joinGame, that.joinGame) && Objects.equals(this.spectateGame, that.spectateGame) && Objects.equals(this.joinRequest, that.joinRequest);
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.ready, this.disconnected, this.errored, this.joinGame, this.spectateGame, this.joinRequest});
    }

    protected List<String> getFieldOrder() {
        return FIELD_ORDER;
    }
}
