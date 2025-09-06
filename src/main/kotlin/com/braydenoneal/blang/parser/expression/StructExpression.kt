package com.braydenoneal.blang.parser.expression

import com.braydenoneal.blang.parser.ParseException
import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.value.StructValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class StructExpression(val expressions: List<Pair<String, Expression>>) : Expression {
    override fun evaluate(program: Program): Value<*> {
        return StructValue(expressions.map { expression -> Pair.of(expression.first, expression.second.evaluate(program)) })
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.LIST_EXPRESSION

    companion object {
        fun parse(program: Program): Expression {
            val expressions: MutableList<Pair<String, Expression>> = ArrayList()
            program.expect(Type.CURLY_BRACE, "{")

            while (!program.peekIs(Type.CURLY_BRACE, "}")) {
                val name = program.next()

                if (name.type != Type.IDENTIFIER) {
                    throw ParseException("Struct key is not an identifier")
                }

                expressions.add(Pair.of(name.value, Expression.parse(program)))

                if (!program.peekIs(Type.CURLY_BRACE, "}")) {
                    program.expect(Type.COMMA)
                }
            }

            program.expect(Type.CURLY_BRACE, "}")
            return StructExpression(expressions)
        }

        val CODEC: MapCodec<StructExpression> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.list(Codec.pair(Codec.STRING, Expression.CODEC)).fieldOf("expressions").forGetter(StructExpression::expressions)
            ).apply(instance, ::StructExpression)
        }
    }
}
