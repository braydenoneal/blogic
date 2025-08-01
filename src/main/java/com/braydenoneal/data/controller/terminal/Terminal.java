package com.braydenoneal.data.controller.terminal;

import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.Function;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;

public interface Terminal {
    Codec<Terminal> CODEC = TerminalType.REGISTRY.getCodec().dispatch("terminal_type", Terminal::getType, TerminalType::codec);

    static Terminal getTerminal(Context context, Either<Terminal, Function> input) throws Exception {
        if (input.left().isPresent()) {
            return input.left().get();
        } else if (input.right().isPresent()) {
            return input.right().get().call(context);
        }

        throw new Exception("Terminal nor function is present");
    }

    String getName();

    String getValueAsString();

    TerminalType<?> getType();
}
