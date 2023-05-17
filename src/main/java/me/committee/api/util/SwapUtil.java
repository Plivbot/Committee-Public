package me.committee.api.util;

import net.minecraft.client.Minecraft;

public class SwapUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void silentSwap(Minecraft mc, int prevSlot, int newSlot, boolean Return) {
        mc.player.inventory.currentItem = newSlot;
        mc.playerController.updateController();

        if (Return)
            mc.player.inventory.currentItem = prevSlot;
    }

    public static void swapToSlot(int slot) {
        if (mc.player.inventory.currentItem != slot) {
            mc.player.inventory.currentItem = slot;
            mc.playerController.updateController();
        }
    }

}
