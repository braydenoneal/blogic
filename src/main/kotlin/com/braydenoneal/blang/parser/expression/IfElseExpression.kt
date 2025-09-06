package com.braydenoneal.blang.parser.expression

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.value.BooleanValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class IfElseExpression(
    val condition: Expression,
    val expressionA: Expression,
    val expressionB: Expression
) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val conditionValue = condition.evaluate(program)

        if (conditionValue is BooleanValue) {
            return if (conditionValue.value) expressionA.evaluate(program) else expressionB.evaluate(program)
        }

        throw RunException("Condition is not a boolean")
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.IF_ELSE_EXPRESSION

    companion object {
        fun parse(program: Program, expressionA: Expression): Expression {
            program.expect(Type.KEYWORD, "if")
            val condition: Expression = Expression.parse(program)
            program.expect(Type.KEYWORD, "else")
            val expressionB: Expression = Expression.parse(program)
            return IfElseExpression(condition, expressionA, expressionB)
        }

        val CODEC: MapCodec<IfElseExpression> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Expression.CODEC.fieldOf("condition").forGetter(IfElseExpression::condition),
                Expression.CODEC.fieldOf("expression_a").forGetter(IfElseExpression::expressionA),
                Expression.CODEC.fieldOf("expression_b").forGetter(IfElseExpression::expressionB)
            ).apply(instance, ::IfElseExpression)
        }
    }
}
