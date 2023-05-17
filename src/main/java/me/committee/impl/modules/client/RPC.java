package me.committee.impl.modules.client;

import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.api.util.discord.DiscordPresence;

public class RPC extends Module {

    public RPC() {
        super("RPC", new String[]{"Discord", "R"}, "A discord presence", Category.CLIENT, true);
        this.setEnabled(true);
    }

    private DiscordPresence discordPresence;

    private final Setting<Boolean> balls = new Setting<>("private", new String[]{"P"}, "hides ur username", false).setID(1);
    private final Setting<Boolean> server = new Setting<>("noserver", new String[]{"S"}, "Toggles the server showing", true).setID(0);

    @Override
    public void onEnable() {
        super.onEnable();
        if (this.discordPresence == null)
            this.discordPresence = new DiscordPresence();

        this.discordPresence.start();
    }
    @Override
    public void onDisable() {
        super.onDisable();
        this.discordPresence.stop();
    }
}
