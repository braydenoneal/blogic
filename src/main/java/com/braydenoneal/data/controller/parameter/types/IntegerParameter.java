package com.braydenoneal.data.controller.parameter.types;

import com.braydenoneal.data.controller.parameter.Parameter;
import com.braydenoneal.data.controller.parameter.ParameterType;
import com.braydenoneal.data.controller.parameter.ParameterTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record IntegerParameter() implements Parameter {
    public static final MapCodec<IntegerParameter> CODEC = RecordCodecBuilder.mapCodec(instance -> null);

    @Override
    public ParameterType<?> getType() {
        return ParameterTypes.INTEGER_PARAMETER;
    }
}
