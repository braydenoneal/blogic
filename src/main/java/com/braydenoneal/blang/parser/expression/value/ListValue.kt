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

        for (i in value().indices) {
            print.append(value()[i].toString())

            if (i < value().size - 1) {
                print.append(", ")
            }
        }

        return "$print]"
    }

    companion object {
        val CODEC: MapCodec<ListValue> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.list(Value.CODEC).fieldOf("value").forGetter(ListValue::value)
            ).apply(instance, ::ListValue)
        }

        fun toIndexValues(program: Program, expressions: MutableList<Expression>): MutableList<Value<*>> {
            val indices: MutableList<Value<*>> = ArrayList()

            for (expression in expressions) {
                indices.add(expression.evaluate(program))
            }

            return indices
        }

        fun setNested(list: ListValue, indexValues: MutableList<out Value<*>>, value: Value<*>): ListValue {
            val newList: MutableList<Value<*>> = ArrayList(list.value())
            val indexValue: Value<*> = indexValues.first()

            for (i in newList.indices) {
                if (indexValue is IntegerValue && indexValue.value() != i) {
                    continue
                }

                val nestedList = newList[i]

                if (indexValues.size > 1 && nestedList is ListValue) {
                    newList[i] = setNested(nestedList, indexValues.subList(1, indexValues.size), value)
                } else {
                    newList[i] = value
                }

                break
            }

            return ListValue(newList)
        }

        fun getNested(list: ListValue, indexValues: MutableList<out Value<*>>): Value<*> {
            val indexValue: Value<*> = indexValues.first()

            for (i in list.value().indices) {
                if (indexValue is IntegerValue && indexValue.value() != i) {
                    continue
                }

                val nestedList = list.value()[i]

                return if (indexValues.size > 1 && nestedList is ListValue) {
                    getNested(nestedList, indexValues.subList(1, indexValues.size))
                } else {
                    list.value()[i]
                }
            }

            throw RunException("Nested value does not exist")
        }
    }
}
