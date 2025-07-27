package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.FunctionType;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.BooleanTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public record NotFunction(Either<Terminal, Function> input) implements Function {
    public static final MapCodec<NotFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("input").forGetter(NotFunction::input)
    ).apply(instance, NotFunction::new));

    @Override
    public Terminal method(World world, BlockPos pos, Map<String, Terminal> variables) throws Exception {
        // TODO:
        //   Check if input is correct type
        //   Call input function if necessary
        return new BooleanTerminal(!BooleanTerminal.getValue(world, pos, variables, input));
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.NOT_FUNCTION;
    }
}
