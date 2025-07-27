package com.braydenoneal.data.controller.function;

import com.braydenoneal.data.controller.terminal.Terminal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public record Context(
        World world,
        BlockPos pos,
        Map<String, Terminal> variables
) {
}
