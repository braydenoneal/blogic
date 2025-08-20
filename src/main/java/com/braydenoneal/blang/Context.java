package com.braydenoneal.blang;

import com.braydenoneal.block.entity.ControllerBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record Context(
        World world,
        BlockPos pos,
        ControllerBlockEntity entity
) {
}
