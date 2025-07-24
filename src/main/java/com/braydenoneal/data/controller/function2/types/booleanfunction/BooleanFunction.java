package com.braydenoneal.data.controller.function2.types.booleanfunction;

import com.mojang.serialization.Codec;

public interface BooleanFunction {
    Codec<BooleanFunction> CODEC = BooleanFunctionType.REGISTRY.getCodec().dispatch("boolean_function_type", BooleanFunction::getType, BooleanFunctionType::codec);

    boolean call();

    BooleanFunctionType<?> getType();
}
