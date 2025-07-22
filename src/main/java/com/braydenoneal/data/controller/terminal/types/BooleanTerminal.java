package com.braydenoneal.data.controller.terminal.types;

import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.TerminalType;
import com.braydenoneal.data.controller.terminal.TerminalTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BooleanTerminal(boolean value) implements Terminal {
    public static final MapCodec<BooleanTerminal> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.fieldOf("value").forGetter(BooleanTerminal::value)
    ).apply(instance, BooleanTerminal::new));

    @Override
    public TerminalType<?> getType() {
        return TerminalTypes.BOOLEAN_TERMINAL;
    }
}
