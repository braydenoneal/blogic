package com.braydenoneal.blang.parser.expression.value

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

class StructValue(value: List<Pair<String, Value<*>>>) : Value<List<Pair<String, Value<*>>>>(value) {
    override val valueType: ValueType<*> get() = ValueTypes.STRUCT

    override fun toString(): String {
        val print = StringBuilder("{\n")

        for (i in value.indices) {
            print.append(value[i].first + ": " + value[i].second)

            if (i < value.size - 1) {
                print.append(",\n")
            }
        }

        return "$print\n}"
    }

    companion object {
        val CODEC: MapCodec<StructValue> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.list(Codec.pair(Codec.STRING, Value.CODEC)).fieldOf("value").forGetter(StructValue::value)
            ).apply(instance, ::StructValue)
        }
    }
}
