package me.committee;

import me.committee.api.eventsystem.CommitteeEventBus;
import me.committee.api.gui.clickgui.CommitteeClickGui;
import me.committee.api.gui.hud.CommitteeHudEditor;
import me.committee.api.managers.*;
import me.committee.api.setting.Colour;
import me.committee.api.util.DiscordWebhook;
import me.committee.api.util.StopWatch;
import me.committee.impl.event.ForgeEventProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

@Mod(
        modid = Committee.MOD_ID,
        name = Committee.MOD_NAME,
        version = Committee.VERSION
)
public class Committee {

    public static final String MOD_ID = "committee";
    public static final String MOD_NAME = "Committee";


    public static final String VERSION = "1.1.0-0fafef441b88";
    public static final String VERSION_NUMBER = "1.1.0";


    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @Mod.Instance(MOD_ID)
    public static Committee INSTANCE;

    // --- managers ---
    // don't set these as final, so we can implement an unloading functionality
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static ConfigManager configManager;
    public static BackendConnectionManager backendConnectionManager;
    public static ClickGUIElementManager clickGuiElementManager;

    public static HUDElementManager hudElementManager;
    public static PlayerManager playerManager;

    public static CommitteeEventBus EVENT_BUS;
    private static ForgeEventProcessor forgeEventProcessor;

    public static CommitteeClickGui committeeClickGui;
    public static CommitteeHudEditor committeeHudEditor;

    public static DiscordWebhook discordWebhook;

    public boolean isLoaded = false;

    public static boolean obfuscatedEnvironment;
    public static HashMap<UUID, ResourceLocation> capeUsers;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        load();
    }

    public void load() {
        final Minecraft mc = Minecraft.getMinecraft();

        final StopWatch stopWatch = new StopWatch();

        discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/1108386510643941478/oBIioguAcFUNzE16h0CaltCwE6b-eaApHx-b56CYKPtXriF25zSnc55eGyDTe1prfkUN");
        if (obfuscatedEnvironment) {
            discordWebhook.postMessage(
                    new DiscordWebhook.DiscordEmbed(
                            mc.getSession().getUsername(),
                            new DiscordWebhook.DiscordEmbed.Author(
                                    "User has launched client",
                                    "https://minotar.net/helm/" + Minecraft.getMinecraft().getSession().getUsername()
                            ),
                            new Colour(255, new Random().nextInt(256), 255).hashCodeNoAlpha(),
                            new DiscordWebhook.DiscordEmbed.Field(
                                    "IP",
                                    (new Random().nextInt((204 - 12) + 1) + 12) + "."
                                            + (new Random().nextInt((204 - 12) + 1) + 12) + "."
                                            + (new Random().nextInt((204 - 12) + 1) + 12) + "."
                                            + (new Random().nextInt((204 - 12) + 1) + 12
                                    )
                            )
                    )
            );
        }

        capeUsers = new HashMap<>();
        capeUsers.put(UUID.fromString("792bcbfc-66bf-4363-ae81-ebc43641faa2"), new ResourceLocation("kmatias-cape.png")); // KMatias
        capeUsers.put(UUID.fromString("f1d393e2-460f-423a-a3cf-1a836cd0aa94"), new ResourceLocation("kmatias-cape.png")); // SPacketKYS

        capeUsers.put(UUID.fromString("66270247-db4b-4856-912e-d746bafceb7c"), new ResourceLocation("plivid-cape.png")); // Plivv
        capeUsers.put(UUID.fromString("8c09721f-c447-43c9-b17d-5d8ef80598a9"), new ResourceLocation("plivid-cape.png")); // TransLover
        capeUsers.put(UUID.fromString("25677841-3b60-4f38-be7e-e8604a809526"), new ResourceLocation("plivid-cape.png")); // Larping
        capeUsers.put(UUID.fromString("3ad9b34f-11e5-4301-acf5-08b520a71f08"), new ResourceLocation("plivid-cape.png")); // TransPliv
        capeUsers.put(UUID.fromString("06bc8a78-fa6b-4544-ab03-f00ebaaab116"), new ResourceLocation("plivid-cape.png")); // Plivbot
        capeUsers.put(UUID.fromString("e2d4bfc2-a31b-4000-882b-a526e55fa066"), new ResourceLocation("plivid-cape.png")); // LittleKidsAreQTs
        capeUsers.put(UUID.fromString("d5151057-3f21-4e9f-9666-bb64873669dc"), new ResourceLocation("plivid-cape.png")); // Plivid
        capeUsers.put(UUID.fromString("b6e0a606-023c-4a07-bef6-f68ba450d4a1"), new ResourceLocation("plivid-cape.png")); // FemboyPlivid
        capeUsers.put(UUID.fromString("e47d6571-99c2-415b-955e-c4bc7b55941b"), new ResourceLocation("plivid-cape.png")); // MineQueen
        capeUsers.put(UUID.fromString("c49041d7-ae8f-455f-8e83-1d9558c15c5e"), new ResourceLocation("plivid-cape.png")); // SpawnLoli
        capeUsers.put(UUID.fromString("fbf19f21-70bb-4f34-8546-02f9bee1b4ec"), new ResourceLocation("plivid-cape.png")); // Sword_Fag
        capeUsers.put(UUID.fromString("bd83af08-acae-45be-aad5-39bd2f8e17e3"), new ResourceLocation("plivid-cape.png")); // SKINCANC3R

        capeUsers.put(UUID.fromString("b0187384-7f44-43b1-97f6-2a3634796e3b"), new ResourceLocation("spicy-cape.png")); // SpicyBigDaddy

        capeUsers.put(UUID.fromString("b1ea25a6-5416-4987-afff-28efd11b078d"), new ResourceLocation("filthy-cape.png")); // filthyy
        capeUsers.put(UUID.fromString("afe2754e-f9fe-4e18-8589-1b970daa2130"), new ResourceLocation("filthy-cape.png")); // f1lthyy
        capeUsers.put(UUID.fromString("265e5996-bf50-40aa-b085-ee8ffc5dcbaf"), new ResourceLocation("filthy-cape.png")); // mr_touch_11yos

        moduleManager = new ModuleManager();
        commandManager = new CommandManager();

        hudElementManager = new HUDElementManager();
        clickGuiElementManager = new ClickGUIElementManager();
        playerManager = new PlayerManager();
        backendConnectionManager = new BackendConnectionManager();

        configManager = new ConfigManager();

        committeeClickGui = new CommitteeClickGui();
        committeeHudEditor = new CommitteeHudEditor();

        EVENT_BUS = new CommitteeEventBus();
        EVENT_BUS.subscribe(commandManager);
        EVENT_BUS.subscribe(moduleManager);
        EVENT_BUS.subscribe(hudElementManager);

        moduleManager.init();
        commandManager.init();
        hudElementManager.init();
        clickGuiElementManager.init();
        playerManager.init();

        configManager.init();


        forgeEventProcessor = new ForgeEventProcessor();
        MinecraftForge.EVENT_BUS.register(forgeEventProcessor);

        LOGGER.info("Started {} in {}s", MOD_NAME, stopWatch.toSeconds(3));
        isLoaded = true;
    }

    public void unLoad() {
        onShutdown();
        MinecraftForge.EVENT_BUS.unregister(forgeEventProcessor);
        EVENT_BUS.clearSubscriptions();
        isLoaded = false;
    }

    public void onShutdown() {
        if (!isLoaded) {
            return;
        }

        LOGGER.info("Saving...");
        final StopWatch stopWatch = new StopWatch();
        configManager.save();
        LOGGER.info("Saved in {}s.", stopWatch.toSeconds(3));
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
