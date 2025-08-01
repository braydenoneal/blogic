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

import java.util.List;

public record ListTerminal(List<Terminal> value) implements Terminal {
    public static final MapCodec<ListTerminal> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.list(Terminal.CODEC).fieldOf("value").forGetter(ListTerminal::value)
            ).apply(instance, ListTerminal::new)
    );

    // TODO: Get the full List<...> with primitives? Or maybe not necessary?
    public static List<Terminal> getValue(Context context, Either<Terminal, Function> input) throws Exception {
        if (Terminal.getTerminal(context, input) instanceof ListTerminal(List<Terminal> value)) {
            return value;
        }

        throw new Exception("Value is not a list");
    }

    @Override
    public String getName() {
        return "List<...>";
    }

    @Override
    public String getValueAsString() {
        return value.toString();
    }

    @Override
    public TerminalType<?> getType() {
        return TerminalTypes.LIST_TERMINAL;
    }
}
