package me.committee.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

public class PlayerUtil {

    public static boolean itemSlot(EntityEquipmentSlot slot, Item item) {
        return Minecraft.getMinecraft().player.getItemStackFromSlot(slot).getItem().equals(item);
    }

}
