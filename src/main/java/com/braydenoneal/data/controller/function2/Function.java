package com.braydenoneal.data.controller.function2;

import com.mojang.serialization.Codec;

public interface Function {
    Codec<Function> CODEC = FunctionType.REGISTRY.getCodec().dispatch("function_type", Function::getType, FunctionType::codec);

    void call();

    FunctionType<?> getType();
}
