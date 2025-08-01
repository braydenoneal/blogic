package com.braydenoneal.data.controller.terminal.types;

import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.TerminalType;
import com.braydenoneal.data.controller.terminal.TerminalTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ErrorTerminal(String value) implements Terminal {
    public static final MapCodec<ErrorTerminal> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.fieldOf("value").forGetter(ErrorTerminal::value)
            ).apply(instance, ErrorTerminal::new)
    );

    @Override
    public String getName() {
        return "Error";
    }

    @Override
    public String getValueAsString() {
        return value;
    }

    @Override
    public TerminalType<?> getType() {
        return TerminalTypes.ERROR_TERMINAL;
    }
}
