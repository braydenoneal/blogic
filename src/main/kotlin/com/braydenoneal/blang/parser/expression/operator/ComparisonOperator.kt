package com.braydenoneal.blang.parser.expression.operator

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.BooleanValue
import com.braydenoneal.blang.parser.expression.value.FloatValue
import com.braydenoneal.blang.parser.expression.value.IntegerValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class ComparisonOperator(
    val operator: String,
    val operandA: Expression,
    val operandB: Expression
) : Operator, Expression {
    override fun evaluate(program: Program): Value<*> {
        val a = operandA.evaluate(program)
        val b = operandB.evaluate(program)

        if (operator == "==") {
            return BooleanValue(a == b)
        } else if (operator == "!=") {
            return BooleanValue(a != b)
        }

        if (a is IntegerValue && b is IntegerValue) {
            return when (operator) {
                "<=" -> BooleanValue(a.value <= b.value)
                ">=" -> BooleanValue(a.value >= b.value)
                "<" -> BooleanValue(a.value < b.value)
                else -> BooleanValue(a.value > b.value)
            }
        } else if (a is FloatValue && b is FloatValue) {
            return when (operator) {
                "<=" -> BooleanValue(a.value <= b.value)
                ">=" -> BooleanValue(a.value >= b.value)
                "<" -> BooleanValue(a.value < b.value)
                else -> BooleanValue(a.value > b.value)
            }
        }

        throw RunException("Operands are not comparable")
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.COMPARISON_OPERATOR

    companion object {
        val CODEC: MapCodec<ComparisonOperator> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.STRING.fieldOf("operator").forGetter(ComparisonOperator::operator),
                Expression.CODEC.fieldOf("operand_a").forGetter(ComparisonOperator::operandA),
                Expression.CODEC.fieldOf("operand_b").forGetter(ComparisonOperator::operandB)
            ).apply(it, ::ComparisonOperator)
        }
    }
}
