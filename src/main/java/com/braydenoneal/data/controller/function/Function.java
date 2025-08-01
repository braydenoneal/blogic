package com.braydenoneal.data.controller.function;

import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.ErrorTerminal;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;

import java.util.Map;

// TODO: Have functions specify their return types and parameter types so that errors can be detected before running
public interface Function {
    Codec<Function> CODEC = FunctionType.REGISTRY.getCodec().dispatch("function_type", Function::getType, FunctionType::codec);

    default Terminal call(Context context) {
        try {
            return method(context);
        } catch (Exception e) {
            return new ErrorTerminal(e.getMessage());
        }
    }

    Terminal method(Context context) throws Exception;

    String getName();

    Map<String, Either<Terminal, Function>> getParameters();

    FunctionType<?> getType();
}
