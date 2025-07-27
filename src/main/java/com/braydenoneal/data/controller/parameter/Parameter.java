package com.braydenoneal.data.controller.parameter;

import com.braydenoneal.data.controller.terminal.Terminal;
import com.mojang.serialization.Codec;

public interface Parameter {
    Codec<Parameter> CODEC = ParameterType.REGISTRY.getCodec().dispatch("parameter_type", Parameter::getType, ParameterType::codec);

    boolean matchesTerminal(Terminal terminal);

    ParameterType<?> getType();
}
