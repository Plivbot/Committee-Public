package me.committee.api.util;

import net.minecraft.block.BlockObsidian;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class InventoryUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();


    public static boolean isObsidian(Item item) {
        if (item instanceof ItemBlock)
            return ((ItemBlock) item).getBlock() instanceof BlockObsidian;

        return false;
    }

    public static int findObsidian(boolean justHotbar) {
        if (isObsidian(mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).getItem()))
            return mc.player.inventory.currentItem;

        int slotsToLookIn = justHotbar ? 9 : 36;

        int Slot;
        for (Slot = 0; Slot < slotsToLookIn; Slot++) {
            ItemStack slots = mc.player.inventory.getStackInSlot(Slot);
            if (isObsidian(slots.getItem())) {
                return Slot;
            }
        }
        return mc.player.inventory.currentItem;
    }

    public static int findAnyBlock(boolean justHotbar) {
        if (mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).getItem() instanceof ItemBlock)
            return mc.player.inventory.currentItem;

        int slotsToLookIn = justHotbar ? 9 : 36;

        int Slot;
        for (Slot = 0; Slot < slotsToLookIn; Slot++) {
            ItemStack slots = mc.player.inventory.getStackInSlot(Slot);
            if (slots.getItem() instanceof ItemBlock) {
                return Slot;
            }
        }
        return mc.player.inventory.currentItem;
    }

    public static int findItem(Item item, boolean justHotbar) {
        if (mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).getItem() == item)
            return mc.player.inventory.currentItem;

        int slotsToLookIn = justHotbar ? 9 : 36;

        int Slot;
        for (Slot = 0; Slot < slotsToLookIn; Slot++) {
            ItemStack slots = mc.player.inventory.getStackInSlot(Slot);
            if (slots.getItem() == item) {
                return Slot;
            }
        }
        return mc.player.inventory.currentItem;
    }

}
