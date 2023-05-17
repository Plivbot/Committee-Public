package me.committee.api.mixin.mixins.accessors;

import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketPlayerTryUseItemOnBlock.class)
public interface AccessorCPacketPlayerTryUseItemOnBlock {

    @Accessor(value = "placedBlockDirection")
    void setPlacedBlockDirection(EnumFacing placedBlockDirection);

}
