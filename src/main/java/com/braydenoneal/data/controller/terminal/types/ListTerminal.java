package com.braydenoneal.data.controller.terminal.types;

import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.TerminalType;
import com.braydenoneal.data.controller.terminal.TerminalTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record ListTerminal(List<Terminal> values) implements Terminal {
    public static final MapCodec<ListTerminal> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.list(Terminal.CODEC).fieldOf("values").forGetter(ListTerminal::values)
    ).apply(instance, ListTerminal::new));

    @Override
    public TerminalType<?> getType() {
        return TerminalTypes.LIST_TERMINAL;
    }
}
