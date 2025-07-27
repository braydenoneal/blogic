package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.FunctionType;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.IntegerTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;

public record ReadRedstoneFunction(
        Either<Terminal, Function> x,
        Either<Terminal, Function> y,
        Either<Terminal, Function> z
) implements Function {
    public static final MapCodec<ReadRedstoneFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("x").forGetter(ReadRedstoneFunction::x),
            Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("y").forGetter(ReadRedstoneFunction::y),
            Codec.either(Terminal.CODEC, Function.CODEC).fieldOf("z").forGetter(ReadRedstoneFunction::z)
    ).apply(instance, ReadRedstoneFunction::new));

    @Override
    public Terminal method(Context context) throws Exception {
        int xValue = IntegerTerminal.getValue(context, x);
        int yValue = IntegerTerminal.getValue(context, y);
        int zValue = IntegerTerminal.getValue(context, z);

        BlockPos readPos = context.pos().add(xValue, yValue, zValue);
        int redstoneValue = context.world().getReceivedRedstonePower(readPos);

        return new IntegerTerminal(redstoneValue);
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.READ_REDSTONE_FUNCTION;
    }
}
