package com.braydenoneal.data.controller.function;

import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.ErrorTerminal;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public interface Function {
    Codec<Function> CODEC = FunctionType.REGISTRY.getCodec().dispatch("function_type", Function::getType, FunctionType::codec);

    default Terminal call(World world, BlockPos pos, Map<String, Terminal> variables) {
        try {
            return method(world, pos, variables);
        } catch (Exception e) {
            return new ErrorTerminal(e.getMessage());
        }
    }

    Terminal method(World world, BlockPos pos, Map<String, Terminal> variables) throws Exception;

    FunctionType<?> getType();
}
