package com.braydenoneal.blang.parser.expression.value

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

class IntegerValue(value: Int) : Value<Int>(value) {
    override val valueType: ValueType<*> get() = ValueTypes.INTEGER

    companion object {
        val CODEC: MapCodec<IntegerValue> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.INT.fieldOf("value").forGetter(IntegerValue::value)
            ).apply(instance, ::IntegerValue)
        }
    }
}
