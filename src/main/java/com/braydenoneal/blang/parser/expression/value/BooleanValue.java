package com.braydenoneal.blang.parser.expression.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class BooleanValue extends Value<Boolean> {
    public static final MapCodec<BooleanValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.fieldOf("value").forGetter(BooleanValue::value)
    ).apply(instance, BooleanValue::new));

    public BooleanValue(Boolean value) {
        super(value);
    }

    @Override
    public ValueType<?> getType() {
        return ValueTypes.BOOLEAN;
    }
}
