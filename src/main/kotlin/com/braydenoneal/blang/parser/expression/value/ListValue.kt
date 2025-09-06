package com.braydenoneal.blang.parser.expression.value

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.Expression
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

class ListValue(value: MutableList<Value<*>>) : Value<MutableList<Value<*>>>(value) {
    override val valueType: ValueType<*> get() = ValueTypes.LIST

    override fun toString(): String {
        val print = StringBuilder("[")

        for (i in value.indices) {
            print.append(value[i].toString())

            if (i < value.size - 1) {
                print.append(", ")
            }
        }

        return "$print]"
    }

    fun get(indexValues: List<Value<*>>): Value<*> {
        if (indexValues.isEmpty()) {
            throw RunException("No indices provided")
        }

        var currentValue: Value<*> = this

        for (index in indexValues) {
            if (index !is IntegerValue) {
                throw RunException("Index is not an integer")
            }

            if (currentValue !is ListValue) {
                throw RunException("Object is not a list")
            }

            if (index.value >= currentValue.value.size) {
                throw RunException("Index " + index.value + " out of range for list of size " + currentValue.value.size)
            }

            currentValue = currentValue.value[index.value]
        }

        return currentValue
    }

    fun set(indexValues: List<Value<*>>, value: Value<*>): Value<*> {
        val list = get(indexValues.subList(0, indexValues.size - 1))
        val lastIndex = indexValues.last()

        if (lastIndex !is IntegerValue) {
            throw RunException("Index is not an integer")
        }
        if (list !is ListValue) {
            throw RunException("Object is not a list")
        }

        list.value[lastIndex.value] = value
        return value
    }

    companion object {
        val CODEC: MapCodec<ListValue> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.list(Value.CODEC).fieldOf("value").forGetter(ListValue::value)
            ).apply(instance, ::ListValue)
        }

        fun toIndexValues(program: Program, expressions: MutableList<Expression>): List<Value<*>> {
            return expressions.map { expression -> expression.evaluate(program) }
        }
    }
}
