package com.braydenoneal.blang.parser.statement

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class ExpressionStatement(val expression: Expression) : Statement {
    override fun execute(program: Program): Statement {
        expression.evaluate(program)
        return this
    }

    override val type: StatementType<*> get() = StatementTypes.EXPRESSION_STATEMENT

    companion object {
        fun parse(program: Program): Statement {
            val expression = Expression.parse(program)
            program.expect(Type.SEMICOLON)
            return ExpressionStatement(expression)
        }

        val CODEC: MapCodec<ExpressionStatement> = RecordCodecBuilder.mapCodec {
            it.group(
                Expression.CODEC.fieldOf("expression").forGetter(ExpressionStatement::expression)
            ).apply(it, ::ExpressionStatement)
        }
    }
}
