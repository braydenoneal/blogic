package com.braydenoneal.data.controller.terminal.types;

import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.TerminalType;
import com.braydenoneal.data.controller.terminal.TerminalTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record VoidTerminal() implements Terminal {
    public static final MapCodec<VoidTerminal> CODEC = RecordCodecBuilder.mapCodec(instance -> null);

    @Override
    public TerminalType<?> getType() {
        return TerminalTypes.VOID_TERMINAL;
    }
}
