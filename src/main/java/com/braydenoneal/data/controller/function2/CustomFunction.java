package com.braydenoneal.data.controller.function2;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

// TODO: Parameters
public record CustomFunction(String name, List<Function> body, Function returnFunction) {
    public static final MapCodec<CustomFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(CustomFunction::name),
            Codec.list(Function.CODEC).fieldOf("body").forGetter(CustomFunction::body),
            Function.CODEC.fieldOf("return").forGetter(CustomFunction::returnFunction)
    ).apply(instance, CustomFunction::new));
}
