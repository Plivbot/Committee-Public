package me.committee.impl.commands;

import me.committee.api.command.Command;
import net.minecraft.network.play.client.CPacketClientStatus;

public class RespawnCommand extends Command {

    public RespawnCommand() {
        super("Respawn", new String[]{}, "Sends a respawn packet.");
    }

    @Override
    public String[][] getCompletions() {
        return new String[0][];
    }

    @Override
    public void onExec(String[] args) {
        if (mc.player == null)
            return;

        mc.player.connection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
    }
}
