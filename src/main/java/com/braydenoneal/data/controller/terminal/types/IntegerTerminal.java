package com.braydenoneal.data.controller.terminal.types;

import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.TerminalType;
import com.braydenoneal.data.controller.terminal.TerminalTypes;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public record IntegerTerminal(int value) implements Terminal {
    public static final MapCodec<IntegerTerminal> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("value").forGetter(IntegerTerminal::value)
    ).apply(instance, IntegerTerminal::new));

    public static int getValue(World world, BlockPos pos, Map<String, Terminal> variables, Either<Terminal, Function> input) throws Exception {
        if (Terminal.getTerminal(world, pos, variables, input) instanceof IntegerTerminal(int value1)) {
            return value1;
        }

        throw new Exception("");
    }

    @Override
    public TerminalType<?> getType() {
        return TerminalTypes.INTEGER_TERMINAL;
    }
}
