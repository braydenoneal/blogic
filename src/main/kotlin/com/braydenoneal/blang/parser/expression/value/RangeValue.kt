package com.braydenoneal.blang.parser.expression.value

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

class RangeValue(value: Range) : Value<Range>(value) {
    override val valueType: ValueType<*> get() = ValueTypes.RANGE

    override fun toString(): String {
        return "range(" + value.start + ", " + value.end + ", " + value.step + ")"
    }

    companion object {
        val CODEC: MapCodec<RangeValue> = RecordCodecBuilder.mapCodec {
            it.group(
                Range.CODEC.fieldOf("value").forGetter(RangeValue::value)
            ).apply(it, ::RangeValue)
        }
    }
}
