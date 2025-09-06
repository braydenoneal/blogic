package com.braydenoneal.blang;

import com.braydenoneal.block.entity.ControllerBlockEntity;
import net.minecraft.util.math.BlockPos;

public record Context(
        BlockPos pos,
        ControllerBlockEntity entity
) {
}
