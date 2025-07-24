package com.braydenoneal.data.controller.function2.types.booleanfunction.types;

import com.braydenoneal.data.controller.function2.types.booleanfunction.BooleanFunction;
import com.braydenoneal.data.controller.function2.types.booleanfunction.BooleanFunctionType;
import com.braydenoneal.data.controller.function2.types.booleanfunction.BooleanFunctionTypes;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record NotFunction(Either<Boolean, BooleanFunction> input) implements BooleanFunction {
    public static final MapCodec<NotFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.either(Codec.BOOL, BooleanFunction.CODEC).fieldOf("input").forGetter(NotFunction::input)
    ).apply(instance, NotFunction::new));

    @Override
    public boolean call() {
        if (input.right().isPresent()) {
            return input.right().get().call();
        }

        return input.left().orElseThrow();
    }

    @Override
    public BooleanFunctionType<?> getType() {
        return BooleanFunctionTypes.NOT_FUNCTION;
    }
}
