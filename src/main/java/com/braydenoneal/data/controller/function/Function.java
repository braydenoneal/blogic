package com.braydenoneal.data.controller.function;

import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.ErrorTerminal;
import com.mojang.serialization.Codec;

public interface Function {
    Codec<Function> CODEC = FunctionType.REGISTRY.getCodec().dispatch("functionType", Function::getType, FunctionType::codec);

    default Terminal call() {
        try {
            return method();
        } catch (Exception e) {
            return new ErrorTerminal(e.getMessage());
        }
    }

    Terminal method() throws Exception;

    FunctionType<?> getType();
}
