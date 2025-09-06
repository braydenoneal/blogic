package com.braydenoneal.blang.parser.expression.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class RangeValue extends Value<Range> {
    public static final MapCodec<RangeValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Range.CODEC.fieldOf("value").forGetter(RangeValue::value)
    ).apply(instance, RangeValue::new));

    public RangeValue(Range value) {
        super(value);
    }

    @Override
    public ValueType<?> getValueType() {
        return ValueTypes.RANGE;
    }

    @Override
    public String toString() {
        return "range(" + value().start() + ", " + value().end() + ", " + value().step() + ")";
    }
}
