package com.braydenoneal.blang.parser.expression.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class NullValue extends Value<Null> {
    public static final MapCodec<NullValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Null.CODEC.fieldOf("null").forGetter(NullValue::value)
    ).apply(instance, NullValue::new));

    public NullValue(Null value) {
        super(value);
    }

    @Override
    public ValueType<?> getValueType() {
        return ValueTypes.NULL;
    }

    @Override
    public String toString() {
        return "null";
    }
}
