package com.braydenoneal.blang.parser.expression.value

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

class StructValue(value: MutableList<Pair<String, Value<*>>>) : Value<MutableList<Pair<String, Value<*>>>>(value) {
    override val valueType: ValueType<*> get() = ValueTypes.STRUCT

    override fun toString(): String {
        val print = StringBuilder("{")

        for (i in value.indices) {
            print.append(value[i].first + ": " + value[i].second)

            if (i < value.size - 1) {
                print.append(", ")
            }
        }

        return "$print}"
    }

    fun get(property: String): Value<*> {
        for (entry in value) {
            if (entry.first == property) {
                return entry.second
            }
        }

        return Null.VALUE
    }

    fun set(property: String, setValue: Value<*>): Value<*> {
        for (i in value.indices) {
            if (value[i].first == property) {
                value[i] = Pair.of(value[i].first, setValue)
                return setValue
            }
        }

        return Null.VALUE
    }

    companion object {
        val CODEC: MapCodec<StructValue> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.list(Codec.pair(Codec.STRING, Value.CODEC)).fieldOf("value").forGetter(StructValue::value)
            ).apply(instance, ::StructValue)
        }
    }
}
