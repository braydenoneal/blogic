package com.braydenoneal.blang.parser.expression.builtin.list

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.BooleanValue
import com.braydenoneal.blang.parser.expression.value.ListValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder


data class ListContainsAllBuiltin(
    val listValue: ListValue,
    val arguments: Arguments
) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val nextListValue = arguments.anyValue(program, "value", 0)

        if (nextListValue is ListValue) {
            return BooleanValue(listValue.value().containsAll(nextListValue.value()))
        }

        throw RunException("Expression is not a list")
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.LIST_CONTAINS_ALL_BUILTIN

    companion object {
        val CODEC: MapCodec<ListContainsAllBuiltin> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                ListValue.CODEC.fieldOf("listValue").forGetter(ListContainsAllBuiltin::listValue),
                Arguments.CODEC.fieldOf("arguments").forGetter(ListContainsAllBuiltin::arguments)
            ).apply(instance, ::ListContainsAllBuiltin)
        }
    }
}
