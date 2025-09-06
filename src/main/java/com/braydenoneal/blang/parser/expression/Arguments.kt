package com.braydenoneal.blang.parser.expression

import com.braydenoneal.blang.parser.ParseException
import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.value.*
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.*
import java.util.Map

data class Arguments(val arguments: MutableList<Expression>, val namedArguments: MutableMap<String, Expression>) {
    fun anyValue(program: Program, name: String, index: Int): Value<*> {
        var expression = namedArguments[name]

        if (expression == null) {
            if (index >= arguments.size) {
                throw RunException("Missing argument $name")
            }

            expression = arguments[index]
        }

        return expression.evaluate(program)
    }

    fun blockValue(program: Program, name: String, index: Int): BlockValue {
        return anyValue(program, name, index) as? BlockValue ?: throw RunException("$name is not a block")
    }

    fun booleanValue(program: Program, name: String, index: Int): BooleanValue {
        return anyValue(program, name, index) as? BooleanValue ?: throw RunException("$name is not a boolean")
    }

    fun floatValue(program: Program, name: String, index: Int): FloatValue {
        return anyValue(program, name, index) as? FloatValue ?: throw RunException("$name is not a float")
    }

    fun functionValue(program: Program, name: String, index: Int): FunctionValue {
        return anyValue(program, name, index) as? FunctionValue ?: throw RunException("$name is not a function")
    }

    fun integerValue(program: Program, name: String, index: Int): IntegerValue {
        return anyValue(program, name, index) as? IntegerValue ?: throw RunException("$name is not a integer")
    }

    fun itemStackValue(program: Program, name: String, index: Int): ItemStackValue {
        return anyValue(program, name, index) as? ItemStackValue ?: throw RunException("$name is not a stack")
    }

    fun itemValue(program: Program, name: String, index: Int): ItemValue {
        return anyValue(program, name, index) as? ItemValue ?: throw RunException("$name is not a item")
    }

    fun listValue(program: Program, name: String, index: Int): ListValue {
        return anyValue(program, name, index) as? ListValue ?: throw RunException("$name is not a list")
    }

    fun rangeValue(program: Program, name: String, index: Int): RangeValue {
        return anyValue(program, name, index) as? RangeValue ?: throw RunException("$name is not a range")
    }

    fun stringValue(program: Program, name: String, index: Int): StringValue {
        return anyValue(program, name, index) as? StringValue ?: throw RunException("$name is not a string")
    }

    companion object {
        fun parse(program: Program): Arguments {
            val arguments: MutableList<Expression> = Stack()
            val namedArguments: MutableMap<String, Expression> = HashMap()
            var parseDefaults = false

            program.expect(Type.PARENTHESIS, "(")

            while (!program.peekIs(Type.PARENTHESIS, ")")) {
                val expression: Expression = Expression.parse(program)

                if (expression is AssignmentExpression) {
                    parseDefaults = true
                }

                if (parseDefaults) {
                    try {
                        val assignmentExpression = expression as AssignmentExpression
                        val variableExpression = assignmentExpression.variableExpression

                        if (variableExpression is VariableExpression && assignmentExpression.operator == "=") {
                            namedArguments.put(variableExpression.name, assignmentExpression.expression)
                        } else {
                            throw ParseException("")
                        }
                    } catch (_: ParseException) {
                        throw ParseException("Function cannot have parameter with default after parameter without default")
                    }
                } else {
                    arguments.add(expression)
                }

                if (!program.peekIs(Type.PARENTHESIS, ")")) {
                    program.expect(Type.COMMA)
                }
            }

            program.expect(Type.PARENTHESIS, ")")
            return Arguments(arguments, namedArguments)
        }

        val EMPTY: Arguments = Arguments(mutableListOf(), Map.of())

        val CODEC: Codec<Arguments> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.list(Expression.CODEC).fieldOf("arguments").forGetter(Arguments::arguments),
                Codec.unboundedMap(Codec.STRING, Expression.CODEC).fieldOf("namedArguments").forGetter(Arguments::namedArguments)
            ).apply(instance, ::Arguments)
        }
    }
}
