package com.braydenoneal.data.controller.terminal;

import com.braydenoneal.data.controller.function.Function;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;

public interface Terminal {
    Codec<Terminal> CODEC = TerminalType.REGISTRY.getCodec().dispatch("terminalType", Terminal::getType, TerminalType::codec);

    static Terminal getTerminal(Either<Terminal, Function> input) throws Exception {
        if (input.left().isPresent()) {
            return input.left().get();
        } else if (input.right().isPresent()) {
            return input.right().get().call();
        }

        throw new Exception("");
    }

    TerminalType<?> getType();
}
