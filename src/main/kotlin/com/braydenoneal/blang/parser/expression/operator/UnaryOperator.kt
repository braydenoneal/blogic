package com.braydenoneal.blang.parser.expression.operator

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.BooleanValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class UnaryOperator(val operand: Expression) : Operator, Expression {
    override fun evaluate(program: Program): Value<*> {
        val value = operand.evaluate(program)

        if (value is BooleanValue) {
            return BooleanValue(!value.value)
        }

        throw RunException("Operand is not a boolean")
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.UNARY_OPERATOR

    companion object {
        val CODEC: MapCodec<UnaryOperator> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Expression.CODEC.fieldOf("operand").forGetter(UnaryOperator::operand)
            ).apply(instance, ::UnaryOperator)
        }

        fun parse(program: Program): Expression {
            program.next()
            return UnaryOperator(Expression.parse(program))
        }
    }
}
