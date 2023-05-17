package me.committee.impl.event.events.blocks;

import me.committee.api.eventsystem.event.Event;
import net.minecraft.util.math.BlockPos;

public class AddCollisionBoxEvent extends Event {

    private final BlockPos blockPos;
    public AddCollisionBoxEvent(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
