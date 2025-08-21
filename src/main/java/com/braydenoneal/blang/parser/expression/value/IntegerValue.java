package com.braydenoneal.blang.parser.expression.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class IntegerValue extends Value<Integer> {
    public static final MapCodec<IntegerValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("value").forGetter(IntegerValue::value)
    ).apply(instance, IntegerValue::new));

    public IntegerValue(Integer value) {
        super(value);
    }

    @Override
    public ValueType<?> getType() {
        return ValueTypes.INTEGER;
    }
}
