package com.braydenoneal.data.controller.function2.types.booleanfunction;

import com.braydenoneal.data.controller.function2.Function;
import com.braydenoneal.data.controller.function2.FunctionType;
import com.braydenoneal.data.controller.function2.FunctionTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BooleanFunctionBox(BooleanFunction booleanFunction) implements Function {
    public static final MapCodec<BooleanFunctionBox> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BooleanFunction.CODEC.fieldOf("function").forGetter(BooleanFunctionBox::booleanFunction)
    ).apply(instance, BooleanFunctionBox::new));

    @Override
    public void call() {
        booleanFunction.call();
    }

    @Override
    public FunctionType<?> getType() {
        return FunctionTypes.BOOLEAN_FUNCTION;
    }
}
