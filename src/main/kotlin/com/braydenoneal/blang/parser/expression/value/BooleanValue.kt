package com.braydenoneal.blang.parser.expression.value

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

class BooleanValue(value: Boolean) : Value<Boolean>(value) {
    override val valueType: ValueType<*> get() = ValueTypes.BOOLEAN

    companion object {
        val CODEC: MapCodec<BooleanValue> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.BOOL.fieldOf("value").forGetter(BooleanValue::value)
            ).apply(instance, ::BooleanValue)
        }
    }
}
