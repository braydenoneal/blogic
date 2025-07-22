package com.braydenoneal.data.controller.terminal;

import com.mojang.serialization.Codec;

public interface Terminal {
    Codec<Terminal> TERMINAL_CODEC = TerminalType.REGISTRY.getCodec().dispatch("type", Terminal::getType, TerminalType::codec);

    TerminalType<?> getType();
}
