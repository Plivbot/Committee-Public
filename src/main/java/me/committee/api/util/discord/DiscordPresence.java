package me.committee.api.util.discord;

import me.committee.Committee;
import me.committee.impl.modules.client.RPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;

import java.util.Objects;

public class DiscordPresence {
    private final DiscordRPC deezNuts;
    private final DiscordRichPresence penis;
    private final DiscordEventHandlers tittyHandler;
    private final long cumTime;

    public DiscordPresence() {
        this.deezNuts = DiscordRPC.INSTANCE;
        this.penis = new DiscordRichPresence();
        this.tittyHandler = new DiscordEventHandlers();
        this.cumTime = System.currentTimeMillis() / 1000L;
    }

    public void start() {
        final Minecraft mc = Minecraft.getMinecraft();
        this.deezNuts.Discord_Initialize("970431550393049118", tittyHandler, true, "");
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                this.penis.startTimestamp = this.cumTime;

                this.penis.details = mc.world == null ? "Main Menu" : "Playing " + (Minecraft.getMinecraft().isSingleplayer() ? "Singleplayer"
                        : ((Boolean)Committee.moduleManager.getModuleByClass(RPC.class).getSettingById(0).getValue() ? "Multiplayer"
                        : Objects.requireNonNull(Minecraft.getMinecraft().getCurrentServerData()).serverIP));

                this.penis.state = !(Boolean) Committee.moduleManager.getModuleByClass(RPC.class).getSettingById(1).getValue() ? "as " + mc.getSession().getProfile().getName() : "as [REDACTED]";
                this.penis.largeImageKey = "logo2";
                this.penis.largeImageText = "Committee";
                this.deezNuts.Discord_UpdatePresence(this.penis);
                this.deezNuts.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
    }

    public void stop() {
        if (Thread.currentThread() != null && !Thread.currentThread().isInterrupted()) {
            Thread.currentThread().interrupt();
        }
        this.deezNuts.Discord_Shutdown();
    }
}