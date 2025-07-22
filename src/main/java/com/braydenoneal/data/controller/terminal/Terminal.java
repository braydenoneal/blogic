package com.braydenoneal.data.controller.terminal;

import com.mojang.serialization.Codec;

public interface Terminal {
    Codec<Terminal> CODEC = TerminalType.REGISTRY.getCodec().dispatch("terminalType", Terminal::getType, TerminalType::codec);

    TerminalType<?> getType();
}
