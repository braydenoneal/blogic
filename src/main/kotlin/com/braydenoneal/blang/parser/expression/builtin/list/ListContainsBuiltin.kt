package com.braydenoneal.blang.parser.expression.builtin.list

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.BooleanValue
import com.braydenoneal.blang.parser.expression.value.ListValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder


data class ListContainsBuiltin(
    val listValue: ListValue,
    val arguments: Arguments
) : Expression {
    override fun evaluate(program: Program): Value<*> {
        return BooleanValue(listValue.value().contains(arguments.anyValue(program, "value", 0).evaluate(program)))
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.LIST_CONTAINS_BUILTIN

    companion object {
        val CODEC: MapCodec<ListContainsBuiltin> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                ListValue.CODEC.fieldOf("listValue").forGetter(ListContainsBuiltin::listValue),
                Arguments.CODEC.fieldOf("arguments").forGetter(ListContainsBuiltin::arguments)
            ).apply(instance, ::ListContainsBuiltin)
        }
    }
}
