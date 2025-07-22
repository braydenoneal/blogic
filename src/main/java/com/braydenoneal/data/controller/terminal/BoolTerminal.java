package com.braydenoneal.data.controller.terminal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BoolTerminal(boolean value) implements Terminal {
    public static final MapCodec<BoolTerminal> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.fieldOf("value").forGetter(BoolTerminal::value)
    ).apply(instance, BoolTerminal::new));

    @Override
    public TerminalType<?> getType() {
        return TerminalTypes.BOOL_TERMINAL;
    }
}
