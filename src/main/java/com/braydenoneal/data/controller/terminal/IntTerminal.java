package com.braydenoneal.data.controller.terminal;

import com.braydenoneal.data.controller.functionold.TerminalTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record IntTerminal(boolean value) implements Terminal {
    public static final MapCodec<IntTerminal> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.fieldOf("value").forGetter(IntTerminal::value)
    ).apply(instance, IntTerminal::new));

    @Override
    public TerminalType<?> getType() {
        return TerminalTypes.INT_TERMINAL;
    }
}
