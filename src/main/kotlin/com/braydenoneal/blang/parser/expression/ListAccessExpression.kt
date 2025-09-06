package com.braydenoneal.blang.parser.expression

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.value.ListValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class ListAccessExpression(val listExpression: Expression, val indices: MutableList<Expression>) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val listValue = listExpression.evaluate(program)

        if (listValue is ListValue) {
            return ListValue.getNested(listValue, ListValue.toIndexValues(program, indices))
        }

        throw RunException("Expression is not a list")
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.LIST_ACCESS_EXPRESSION

    companion object {
        val CODEC: MapCodec<ListAccessExpression> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Expression.CODEC.fieldOf("listExpression").forGetter(ListAccessExpression::listExpression),
                Codec.list(Expression.CODEC).fieldOf("indices").forGetter(ListAccessExpression::indices)
            ).apply(instance, ::ListAccessExpression)
        }
    }
}
