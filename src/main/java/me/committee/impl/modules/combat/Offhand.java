package me.committee.impl.modules.combat;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;

public class Offhand extends Module {

    private final Setting<Float> health = new Setting<>("Health", "The health at which the totem will be put into the offhand slot.", 15f, 2f, 36f, 0.5f);

    public Offhand() {
        super("AutoTotem", new String[]{"Offhand"}, "Automatically puts a totem in your offhand.", Category.COMBAT);
    }

    @EventSubscribe
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)
            return;

        final int totemSlot = this.getTotemSlot();

        if (totemSlot != -1 && mc.player.getHealth() <= health.getValue()) {
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, totemSlot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, totemSlot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.updateController();
        }

    }

    private int getTotemSlot() {
        for (int i = 0; i < 36; i++) {
            final Item slotItem = mc.player.inventory.getStackInSlot(i).getItem();
            if (slotItem == Items.TOTEM_OF_UNDYING) {
                if (i < 9)
                    i += 36;
                return i;
            }
        }
        return -1;
    }

}
