package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.FunctionType;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.VoidTerminal;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

// TODO: This doesn't work for some reason
public record EmptyFunction() implements Function {
    public static final MapCodec<EmptyFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> null);

    @Override
    public Terminal method(World world, BlockPos pos, Map<String, Terminal> variables) {
        return new VoidTerminal();
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.EMPTY_FUNCTION;
    }
}
