package com.braydenoneal.blang.parser.statement

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.value.Null
import com.braydenoneal.blang.parser.expression.value.Value
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class ReturnStatement(val expression: Expression) : Statement {
    override fun execute(program: Program): Statement {
        return this
    }

    fun returnValue(program: Program): Value<*> {
        return expression.evaluate(program)
    }

    override val type: StatementType<*> get() = StatementTypes.RETURN_STATEMENT

    companion object {
        fun parse(program: Program): Statement {
            program.expect(Type.KEYWORD, "return")
            val expression = if (program.peek().type == Type.SEMICOLON) Null.VALUE else Expression.parse(program)
            program.expect(Type.SEMICOLON)
            return ReturnStatement(expression)
        }

        val CODEC: MapCodec<ReturnStatement> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Expression.CODEC.fieldOf("expression").forGetter(ReturnStatement::expression)
            ).apply(instance, ::ReturnStatement)
        }
    }
}
