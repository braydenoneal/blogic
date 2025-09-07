package com.braydenoneal.blang.parser.expression

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.value.ListValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class ListExpression(val expressions: MutableList<Expression>) : Expression {
    override fun evaluate(program: Program): Value<*> {
        return ListValue(ListValue.toIndexValues(program, expressions) as MutableList<Value<*>>)
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.LIST_EXPRESSION

    companion object {
        fun parse(program: Program): Expression {
            val expressions: MutableList<Expression> = ArrayList()
            program.expect(Type.SQUARE_BRACE, "[")

            while (!program.peekIs(Type.SQUARE_BRACE, "]")) {
                expressions.add(Expression.parse(program))

                if (!program.peekIs(Type.SQUARE_BRACE, "]")) {
                    program.expect(Type.COMMA)
                }
            }

            program.expect(Type.SQUARE_BRACE, "]")
            return ListExpression(expressions)
        }

        val CODEC: MapCodec<ListExpression> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.list(Expression.CODEC).fieldOf("expressions").forGetter(ListExpression::expressions)
            ).apply(it, ::ListExpression)
        }
    }
}
