package com.braydenoneal.data.controller.terminal.types;

import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.TerminalType;
import com.braydenoneal.data.controller.terminal.TerminalTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record IntegerTerminal(boolean value) implements Terminal {
    public static final MapCodec<IntegerTerminal> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.fieldOf("value").forGetter(IntegerTerminal::value)
    ).apply(instance, IntegerTerminal::new));

    @Override
    public TerminalType<?> getType() {
        return TerminalTypes.INTEGER_TERMINAL;
    }
}
