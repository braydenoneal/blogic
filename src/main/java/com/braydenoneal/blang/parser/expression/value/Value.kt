package com.braydenoneal.blang.parser.expression.value

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec

abstract class Value<T>(private val value: T) : Expression {
    fun value(): T {
        return value
    }

    override fun evaluate(program: Program): Value<*> {
        return this
    }

    override fun toString(): String {
        return value().toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Value<*>) {
            return value == other.value()
        }

        return super.equals(other)
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.VALUE

    abstract val valueType: ValueType<*>

    companion object {
        val CODEC: Codec<Value<*>> = ValueType.REGISTRY.getCodec().dispatch("type", Value<*>::valueType, ValueType<*>::codec)
        val MAP_CODEC: MapCodec<Value<*>> = CODEC.fieldOf("value")
    }

    override fun hashCode(): Int {
        var result = value?.hashCode() ?: 0
        result = 31 * result + type.hashCode()
        result = 31 * result + valueType.hashCode()
        return result
    }
}
