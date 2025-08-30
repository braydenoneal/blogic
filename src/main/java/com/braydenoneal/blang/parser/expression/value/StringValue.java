package com.braydenoneal.blang.parser.expression.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class StringValue extends Value<String> {
    public static final MapCodec<StringValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("value").forGetter(StringValue::value)
    ).apply(instance, StringValue::new));

    public StringValue(String value) {
        super(value);
    }

    @Override
    public ValueType<?> getValueType() {
        return ValueTypes.STRING;
    }

    @Override
    public String toString() {
        return "\"" + value() + "\"";
    }
}
