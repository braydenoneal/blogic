package com.braydenoneal.blang.parser.expression.value

import com.braydenoneal.blang.parser.ParseException
import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.statement.ReturnStatement
import com.braydenoneal.blang.parser.statement.Statement
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

class FunctionValue(value: Funct) : Value<Funct>(value) {
    fun call(program: Program, arguments: Arguments): Value<*> {
        return value().call(program, arguments)
    }

    override val valueType: ValueType<*> get() = ValueTypes.FUNCTION

    override fun toString(): String {
        return "fn" + value().parameters.toString() + ": " + value().statements.toString()
    }

    companion object {
        val CODEC: MapCodec<FunctionValue> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Funct.CODEC.fieldOf("value").forGetter(FunctionValue::value)
            ).apply(instance, ::FunctionValue)
        }

        fun parse(program: Program): Expression {
            val parameters: MutableList<String> = ArrayList()
            val defaultParameters: MutableList<Pair<String, Expression>> = ArrayList()
            var parseDefaults = false

            program.expect(Type.KEYWORD, "fn")

            while (program.peek().type !== Type.COLON) {
                val parameterName = program.expect(Type.IDENTIFIER)

                if (program.peekIs(Type.ASSIGN, "=")) {
                    parseDefaults = true
                }

                if (parseDefaults) {
                    try {
                        program.expect(Type.ASSIGN, "=")
                        defaultParameters.add(Pair.of(parameterName, Expression.parse(program)))
                    } catch (_: ParseException) {
                        throw ParseException("Function cannot have parameter with default after parameter without default")
                    }
                } else {
                    parameters.add(parameterName)
                }

                if (program.peek().type !== Type.COLON) {
                    program.expect(Type.COMMA)
                }
            }

            program.expect(Type.COLON)
            val statements: MutableList<Statement> = ArrayList()

            if (program.peekIs(Type.CURLY_BRACE, "{")) {
                program.next()

                while (!program.peekIs(Type.CURLY_BRACE, "}")) {
                    statements.add(Statement.parse(program))
                }

                program.expect(Type.CURLY_BRACE, "}")
            } else {
                statements.add(ReturnStatement(Expression.parse(program)))
            }

            return FunctionValue(Funct(parameters, defaultParameters, statements))
        }
    }
}
