package com.braydenoneal.data.controller.function.types;

import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.FunctionType;
import com.braydenoneal.data.controller.function.FunctionTypes;
import com.braydenoneal.data.controller.function.Parameter;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.IntegerTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public record ReadRedstoneFunction(
        Either<Terminal, Function> x,
        Either<Terminal, Function> y,
        Either<Terminal, Function> z
) implements Function {
    public static final MapCodec<ReadRedstoneFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Parameter.CODEC.fieldOf("x").forGetter(ReadRedstoneFunction::x),
            Parameter.CODEC.fieldOf("y").forGetter(ReadRedstoneFunction::y),
            Parameter.CODEC.fieldOf("z").forGetter(ReadRedstoneFunction::z)
    ).apply(instance, ReadRedstoneFunction::new));

    @Override
    public Terminal method(World world, BlockPos pos, Map<String, Terminal> variables) throws Exception {
        int xValue = IntegerTerminal.getValue(world, pos, variables, x);
        int yValue = IntegerTerminal.getValue(world, pos, variables, y);
        int zValue = IntegerTerminal.getValue(world, pos, variables, z);

        BlockPos readPos = pos.add(xValue, yValue, zValue);
        int redstoneValue = world.getReceivedRedstonePower(readPos);

        return new IntegerTerminal(redstoneValue);
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.READ_REDSTONE_FUNCTION;
    }
}
