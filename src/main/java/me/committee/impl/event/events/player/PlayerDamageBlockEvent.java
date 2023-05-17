package me.committee.impl.event.events.player;

import me.committee.api.eventsystem.event.Event;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class PlayerDamageBlockEvent extends Event {

    private final BlockPos blockPos;
    private final EnumFacing enumFacing;

    public PlayerDamageBlockEvent(BlockPos blockPos, EnumFacing enumFacing) {
        this.blockPos = blockPos;
        this.enumFacing = enumFacing;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public EnumFacing getEnumFacing() {
        return enumFacing;
    }

}
