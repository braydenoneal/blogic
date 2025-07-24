package com.braydenoneal.data.controller.function2;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Map;

public record CustomFunction(
        String name,
        Map<String, Class<?>> parameters,
        List<Function> body,
        Function returnFunction
) {
    public static final MapCodec<CustomFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(CustomFunction::name),
            Codec.unboundedMap(Codec.STRING, Parameters.CODEC).fieldOf("parameters").forGetter(CustomFunction::parameters),
            Codec.list(Function.CODEC).fieldOf("body").forGetter(CustomFunction::body),
            Function.CODEC.fieldOf("return").forGetter(CustomFunction::returnFunction)
    ).apply(instance, CustomFunction::new));
}
