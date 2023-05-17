package me.committee.impl.modules.misc;

import me.committee.api.eventsystem.annotation.EventSubscribe;
import me.committee.api.mixin.mixins.accessors.AccessorMinecraft;
import me.committee.api.module.Module;
import me.committee.api.setting.Setting;
import me.committee.impl.event.events.player.PlayerUpdateEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemExpBottle;

public class FastUse extends Module {

    private final Setting<Boolean> blocks = new Setting<>("Blocks", new String[] {"B", "AA"}, "Makes you place blocks fast.", false );

    public FastUse() {super ("FastUse", new String[]{"FastPlace", "FastEXP", "FastXP"}, "Makes you use certain things faster.", Category.MISC); }

    @EventSubscribe
    public void onPlayerUpdate(PlayerUpdateEvent event){
        final Item mainHand = mc.player.getHeldItemMainhand().getItem();
        final Item offHand = mc.player.getHeldItemOffhand().getItem();
        if (mainHand instanceof ItemExpBottle || offHand instanceof ItemExpBottle ||
                (this.blocks.getValue() && (mainHand instanceof ItemBlock || offHand instanceof ItemBlock))) {
            ((AccessorMinecraft) mc).setRightClickDelayTimer(0);
        }
    }
}

