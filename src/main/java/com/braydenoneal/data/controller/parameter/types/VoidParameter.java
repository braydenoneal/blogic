package com.braydenoneal.data.controller.parameter.types;

import com.braydenoneal.data.controller.parameter.Parameter;
import com.braydenoneal.data.controller.parameter.ParameterType;
import com.braydenoneal.data.controller.parameter.ParameterTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record VoidParameter() implements Parameter {
    public static final MapCodec<VoidParameter> CODEC = RecordCodecBuilder.mapCodec(instance -> null);

    @Override
    public ParameterType<?> getType() {
        return ParameterTypes.VOID_PARAMETER;
    }
}
