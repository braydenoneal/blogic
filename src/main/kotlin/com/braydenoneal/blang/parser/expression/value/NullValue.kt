package com.braydenoneal.blang.parser.expression.value

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

class NullValue(value: Null) : Value<Null>(value) {
    override val valueType: ValueType<*> get() = ValueTypes.NULL

    override fun toString(): String {
        return "null"
    }

    companion object {
        val CODEC: MapCodec<NullValue> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Null.CODEC.fieldOf("null").forGetter(NullValue::value)
            ).apply(instance, ::NullValue)
        }
    }
}
