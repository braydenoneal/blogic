package com.braydenoneal.data.controller.parameter;

import com.mojang.serialization.Codec;

public interface Parameter {
    Codec<Parameter> CODEC = ParameterType.REGISTRY.getCodec().dispatch("parameter_type", Parameter::getType, ParameterType::codec);

    ParameterType<?> getType();
}
