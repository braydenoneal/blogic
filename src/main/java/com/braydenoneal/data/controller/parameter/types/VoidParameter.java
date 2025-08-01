package com.braydenoneal.data.controller.parameter.types;

import com.braydenoneal.data.controller.parameter.Parameter;
import com.braydenoneal.data.controller.parameter.ParameterType;
import com.braydenoneal.data.controller.parameter.ParameterTypes;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.VoidTerminal;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public record VoidParameter() implements Parameter {
    public static final MapCodec<VoidParameter> CODEC = MapCodec.assumeMapUnsafe(Codec.unit(new VoidParameter()));

    @Override
    public boolean matchesTerminal(Terminal terminal) {
        return terminal instanceof VoidTerminal;
    }

    @Override
    public ParameterType<?> getType() {
        return ParameterTypes.VOID_PARAMETER;
    }

    @Override
    public String getName() {
        return "Void";
    }
}
