package com.braydenoneal.blang.parser.expression.operator

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.BooleanValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class BooleanOperator(
    val operator: String,
    val operandA: Expression,
    val operandB: Expression
) : Operator, Expression {
    override fun evaluate(program: Program): Value<*> {
        val a = operandA.evaluate(program)
        val b = operandB.evaluate(program)

        if (a is BooleanValue && b is BooleanValue) {
            if (operator == "and") {
                return BooleanValue(a.value && b.value)
            }

            return BooleanValue(a.value || b.value)
        }

        throw RunException("Operands are not booleans")
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.BOOLEAN_OPERATOR

    companion object {
        val CODEC: MapCodec<BooleanOperator> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.STRING.fieldOf("operator").forGetter(BooleanOperator::operator),
                Expression.CODEC.fieldOf("operand_a").forGetter(BooleanOperator::operandA),
                Expression.CODEC.fieldOf("operand_b").forGetter(BooleanOperator::operandB)
            ).apply(it, ::BooleanOperator)
        }
    }
}
