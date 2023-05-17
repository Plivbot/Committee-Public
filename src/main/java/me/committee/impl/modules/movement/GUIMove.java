package me.committee.impl.modules.movement;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.api.util.MessageSendHelper;
import me.committee.impl.event.events.input.MoveEvent;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class GUIMove extends Module {

    public GUIMove() {
        super("GUIMove", new String[]{""}, "lets u move in guis", Category.MOVEMENT);
    }

    private final Setting<Integer> rotateSpeed = new Setting<Integer>("RotationSpeed", new String[]{"Spd"}, "Speed of flight to travel at.", 5, 1, 10, 1);


    @EventSubscribe
    public void onMove(MoveEvent event) {
        if (mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiScreenBook || mc.currentScreen instanceof GuiEditSign || mc.currentScreen == null && mc.player != null && mc.world != null)
            return;

        final KeyBinding[] binds = {mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSneak};

        for (KeyBinding k : binds) {
            KeyBinding.setKeyBindState(k.getKeyCode(), Keyboard.isKeyDown(k.getKeyCode()));
        }

        if (Keyboard.isKeyDown(200))
            mc.player.rotationPitch -= rotateSpeed.getValue();

        if (Keyboard.isKeyDown(208))
            mc.player.rotationPitch += rotateSpeed.getValue();

        if (Keyboard.isKeyDown(203))
            mc.player.rotationYaw -= rotateSpeed.getValue();

        if (Keyboard.isKeyDown(205))
            mc.player.rotationYaw += rotateSpeed.getValue();
    }

}
