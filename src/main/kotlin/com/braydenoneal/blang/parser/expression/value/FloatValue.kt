package com.braydenoneal.blang.parser.expression.value

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

class FloatValue(value: Float) : Value<Float>(value) {
    override val valueType: ValueType<*> get() = ValueTypes.FLOAT

    companion object {
        val CODEC: MapCodec<FloatValue> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.FLOAT.fieldOf("value").forGetter(FloatValue::value)
            ).apply(it, ::FloatValue)
        }
    }
}
