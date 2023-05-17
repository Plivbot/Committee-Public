package me.committee.impl.modules.combat;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.network.SendPacketEvent;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

public class Criticals extends Module {

    public Criticals() {
        super("Criticals", new String[]{"crits"}, "Makes the player do criticals when standing on solid ground", Category.COMBAT);
    }

    private final Setting<Mode> modeSetting = new Setting<>("Mode", new String[]{"M"}, "Which NCP to target", Mode.NCP);

    @EventSubscribe
    public void onSendPacket(SendPacketEvent event) {
        if (event.getPacket() instanceof CPacketUseEntity && mc.player.onGround &&
                ((CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK) {
            switch (modeSetting.getValue()) {
                case NCP:
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1f, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                    break;
                case UPDATED_NCP:
                    // todo: does this actually work??
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1337f, mc.player.posZ, true));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.05f, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                    break;
            }
        }
    }

    private enum Mode {
        UPDATED_NCP,
        NCP
    }
}
