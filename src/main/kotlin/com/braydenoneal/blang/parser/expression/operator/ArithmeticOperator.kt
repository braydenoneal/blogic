package com.braydenoneal.blang.parser.expression.operator

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.*
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.stream.Stream
import kotlin.math.floor
import kotlin.math.pow

data class ArithmeticOperator(
    val operator: String,
    val operandA: Expression,
    val operandB: Expression
) : Operator, Expression {
    override fun evaluate(program: Program): Value<*> {
        var a = operandA.evaluate(program)
        var b = operandB.evaluate(program)

        if (a is IntegerValue && b is FloatValue) {
            a = FloatValue(a.value.toFloat())
        } else if (a is FloatValue && b is IntegerValue) {
            b = FloatValue(b.value.toFloat())
        } else if (b is StringValue) {
            a = StringValue(a.value.toString())
        } else if (a is StringValue) {
            b = StringValue(b.value.toString())
        }

        if (a is IntegerValue && b is IntegerValue) {
            return when (operator) {
                "+" -> IntegerValue(a.value + b.value)
                "-" -> IntegerValue(a.value - b.value)
                "*" -> IntegerValue(a.value * b.value)
                "//", "/" -> IntegerValue(a.value / b.value)
                "%" -> IntegerValue((a.value + b.value) % b.value)
                else -> IntegerValue(a.value.toDouble().pow(b.value.toDouble()).toInt())
            }
        } else if (a is FloatValue && b is FloatValue) {
            return when (operator) {
                "+" -> FloatValue(a.value + b.value)
                "-" -> FloatValue(a.value - b.value)
                "*" -> FloatValue(a.value * b.value)
                "//" -> FloatValue(floor((a.value / b.value).toDouble()).toFloat())
                "/" -> FloatValue(a.value / b.value)
                "%" -> FloatValue((a.value + b.value) % b.value)
                else -> FloatValue(a.value.toDouble().pow(b.value.toDouble()).toFloat())
            }
        } else if (operator == "+" && a is StringValue && b is StringValue) {
            return StringValue(a.value + b.value)
        } else if (operator == "+" && a is ListValue && b is ListValue) {
            return ListValue(Stream.concat(a.value.stream(), b.value.stream()).toList())
        }

        throw RunException("Invalid operands")
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.ARITHMETIC_OPERATOR

    companion object {
        val CODEC: MapCodec<ArithmeticOperator> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf("operator").forGetter(ArithmeticOperator::operator),
                Expression.CODEC.fieldOf("operand_a").forGetter(ArithmeticOperator::operandA),
                Expression.CODEC.fieldOf("operand_b").forGetter(ArithmeticOperator::operandB)
            ).apply(instance, ::ArithmeticOperator)
        }
    }
}
