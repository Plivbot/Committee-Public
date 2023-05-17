
package me.committee.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class BlockUtil {

    public static EnumFacing findAvailableSide(BlockPos pos) {
        for (EnumFacing e : EnumFacing.VALUES) {
            if (!Minecraft.getMinecraft().world.isAirBlock(pos.offset(e))) {
                return e;
            }
            //break;
        }
        return null;

    }

}

