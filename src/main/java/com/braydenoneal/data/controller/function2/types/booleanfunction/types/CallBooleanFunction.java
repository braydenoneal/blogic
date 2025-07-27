package com.braydenoneal.data.controller.function2.types.booleanfunction.types;

import com.braydenoneal.data.controller.function2.types.booleanfunction.BooleanFunction;
import com.braydenoneal.data.controller.function2.types.booleanfunction.BooleanFunctionType;
import com.braydenoneal.data.controller.function2.types.booleanfunction.BooleanFunctionTypes;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CallBooleanFunction(Either<Boolean, BooleanFunction> input) implements BooleanFunction {
    public static final MapCodec<CallBooleanFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.either(Codec.BOOL, BooleanFunction.CODEC).fieldOf("input").forGetter(CallBooleanFunction::input)
    ).apply(instance, CallBooleanFunction::new));

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
