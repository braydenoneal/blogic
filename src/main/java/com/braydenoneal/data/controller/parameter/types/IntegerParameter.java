package com.braydenoneal.data.controller.parameter.types;

import com.braydenoneal.data.controller.parameter.Parameter;
import com.braydenoneal.data.controller.parameter.ParameterType;
import com.braydenoneal.data.controller.parameter.ParameterTypes;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.BooleanTerminal;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public record IntegerParameter() implements Parameter {
    public static final MapCodec<IntegerParameter> CODEC = MapCodec.assumeMapUnsafe(Codec.unit(new IntegerParameter()));

    @Override
    public boolean matchesTerminal(Terminal terminal) {
        return terminal instanceof BooleanTerminal;
    }

    @Override
    public ParameterType<?> getType() {
        return ParameterTypes.INTEGER_PARAMETER;
    }

    @Override
    public String getName() {
        return "Integer";
    }
}
