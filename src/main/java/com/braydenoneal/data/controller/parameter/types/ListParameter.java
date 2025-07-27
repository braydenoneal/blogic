package com.braydenoneal.data.controller.parameter.types;

import com.braydenoneal.data.controller.parameter.Parameter;
import com.braydenoneal.data.controller.parameter.ParameterType;
import com.braydenoneal.data.controller.parameter.ParameterTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ListParameter(Parameter type) implements Parameter {
    public static final MapCodec<ListParameter> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Parameter.CODEC.fieldOf("type").forGetter(ListParameter::type)
    ).apply(instance, ListParameter::new));

    @Override
    public ParameterType<?> getType() {
        return ParameterTypes.LIST_PARAMETER;
    }
}
