package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.*
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder


data class TypeBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val value = arguments.anyValue(program, "value", 0)

        val string = when (value) {
            is BlockValue -> "block"
            is BooleanValue -> "boolean"
            is FloatValue -> "float"
            is FunctionValue -> "function"
            is IntegerValue -> "integer"
            is ItemStackValue -> "itemStack"
            is ItemValue -> "item"
            is ListValue -> "list"
            is NullValue -> "null"
            is RangeValue -> "range"
            is StringValue -> "string"
            is StructValue -> "struct"
            else -> "null"
        }

        return StringValue(string)
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.TYPE_BUILTIN

    companion object {
        val CODEC: MapCodec<TypeBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(TypeBuiltin::arguments)
            ).apply(it, ::TypeBuiltin)
        }
    }
}
