package com.braydenoneal.blang.parser.expression.value

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

class StringValue(value: String) : Value<String>(value) {
    override val valueType: ValueType<*> get() = ValueTypes.STRING

    override fun toString(): String {
        return "\"" + value() + "\""
    }

    companion object {
        val CODEC: MapCodec<StringValue> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf("value").forGetter(StringValue::value)
            ).apply(instance, ::StringValue)
        }
    }
}
