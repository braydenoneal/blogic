package com.braydenoneal.data.controller.terminal.types;

import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.TerminalType;
import com.braydenoneal.data.controller.terminal.TerminalTypes;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record IntegerTerminal(int value) implements Terminal {
    public static final MapCodec<IntegerTerminal> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT.fieldOf("value").forGetter(IntegerTerminal::value)
            ).apply(instance, IntegerTerminal::new)
    );

    public static int getValue(Context context, Either<Terminal, Function> input) throws Exception {
        if (Terminal.getTerminal(context, input) instanceof IntegerTerminal(int value)) {
            return value;
        }

        throw new Exception("Value is not an integer");
    }

    @Override
    public TerminalType<?> getType() {
        return TerminalTypes.INTEGER_TERMINAL;
    }
}
