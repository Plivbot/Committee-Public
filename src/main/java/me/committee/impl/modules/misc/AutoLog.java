package me.committee.impl.modules.misc;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import net.minecraft.network.play.client.CPacketHeldItemChange;


public class AutoLog extends Module {

    private final Setting<AutoLog.Mode> mode = new Setting<>("Mode", new String[]{"M"}, "The mode to use.", AutoLog.Mode.HEALTH);
    private final Setting<Float> Health = new Setting<>("Health", new String[]{"Spd"},"The health you log out at", 5f, 1f, 20f, 1f);

    public AutoLog() {super("AutoLog", new String[]{"AL"}, "Logs you out at a ceratain health or with a keybind", Category.MISC);}


    @EventSubscribe
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (mode.getValue() == Mode.HEALTH && mc.player.getHealth() <= Health.getValue()) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(-1337));
            this.toggle();
        }
        if (mode.getValue() == Mode.BIND) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(69));
            this.toggle();
        }
    }


    private enum Mode {
        HEALTH, BIND
    }
}
