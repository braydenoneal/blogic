package com.braydenoneal.blang.parser.expression

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.operator.ArithmeticOperator
import com.braydenoneal.blang.parser.expression.value.ListValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class AssignmentExpression(
    val operator: String,
    val variableExpression: Expression,
    val expression: Expression
) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val value = expression.evaluate(program)

        if (variableExpression is VariableExpression) {
            if (operator == "=") {
                return program.scope.set(variableExpression.name, value)
            }

            val prev = program.scope.get(variableExpression.name) ?: throw RunException("Variable '" + variableExpression.name + "' does not exist")
            return program.scope.set(variableExpression.name, ArithmeticOperator(if (operator == "+=") "+" else "-", prev, value).evaluate(program))
        } else if (variableExpression is NamedListAccessExpression) {
            val listValue: Value<*> = variableExpression.variableExpression.evaluate(program)

            if (listValue is ListValue) {
                val indexValues = ListValue.toIndexValues(program, variableExpression.indices)

                if (operator == "=") {
                    return listValue.set(indexValues, value)
                }

                val prev = listValue.get(indexValues)
                return listValue.set(indexValues, ArithmeticOperator(if (operator == "+=") "+" else "-", prev, value).evaluate(program))
            }
        }

        throw RunException("Expression is not a variable nor named list access")
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.ASSIGNMENT_EXPRESSION

    companion object {
        fun parse(program: Program, variableExpression: Expression): Expression {
            val type = program.expect(Type.ASSIGN)
            val expression = Expression.parse(program)
            return AssignmentExpression(type, variableExpression, expression)
        }

        val CODEC: MapCodec<AssignmentExpression> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf("type").forGetter(AssignmentExpression::operator),
                Expression.CODEC.fieldOf("variable_expression").forGetter(AssignmentExpression::variableExpression),
                Expression.CODEC.fieldOf("expression").forGetter(AssignmentExpression::variableExpression)
            ).apply(instance, ::AssignmentExpression)
        }
    }
}
