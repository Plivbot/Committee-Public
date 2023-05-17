package me.committee.api.mixin.mixins.accessors;

import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Timer.class)
public interface AccessorTimer {

    @Accessor(value = "tickLength")
    void setTickLength(float tickLength);

    @Accessor(value = "tickLength")
    float getTickLength();


}
