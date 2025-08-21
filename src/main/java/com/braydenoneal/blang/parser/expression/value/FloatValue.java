package com.braydenoneal.blang.parser.expression.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class FloatValue extends Value<Float> {
    public static final MapCodec<FloatValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("value").forGetter(FloatValue::value)
    ).apply(instance, FloatValue::new));

    public FloatValue(Float value) {
        super(value);
    }

    @Override
    public ValueType<?> getType() {
        return ValueTypes.FLOAT;
    }
}
