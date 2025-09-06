package com.braydenoneal.blang.parser.statement

import com.braydenoneal.blang.parser.ParseException
import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.value.Funct
import com.braydenoneal.blang.parser.expression.value.Value
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class FunctionDeclaration(val name: String, val function: Funct) : Statement {
    override fun execute(program: Program): Statement {
        return this
    }

    fun call(program: Program, arguments: Arguments): Value<*> {
        return this.function.call(program, arguments)
    }

    override val type: StatementType<*> get() = StatementTypes.FUNCTION_DECLARATION

    companion object {
        fun parse(program: Program): Statement {
            val parameters: MutableList<String> = ArrayList()
            val defaultParameters: MutableList<Pair<String, Expression>> = ArrayList()
            var parseDefaults = false

            program.expect(Type.KEYWORD, "fn")
            val name = program.expect(Type.IDENTIFIER)
            program.expect(Type.PARENTHESIS, "(")


            while (!program.peekIs(Type.PARENTHESIS, ")")) {
                val parameterName = program.expect(Type.IDENTIFIER)

                if (program.peekIs(Type.ASSIGN, "=")) {
                    parseDefaults = true
                }

                if (parseDefaults) {
                    try {
                        program.expect(Type.ASSIGN, "=")
                        defaultParameters.add(Pair.of<String, Expression>(parameterName, Expression.parse(program)))
                    } catch (_: ParseException) {
                        throw ParseException("Function cannot have parameter with default after parameter without default")
                    }
                } else {
                    parameters.add(parameterName)
                }

                if (!program.peekIs(Type.PARENTHESIS, ")")) {
                    program.expect(Type.COMMA)
                }
            }

            program.expect(Type.PARENTHESIS, ")")
            program.expect(Type.CURLY_BRACE, "{")
            val statements: MutableList<Statement> = ArrayList()

            while (!program.peekIs(Type.CURLY_BRACE, "}")) {
                statements.add(Statement.parse(program))
            }

            program.expect(Type.CURLY_BRACE, "}")

            val functionDeclaration = FunctionDeclaration(name, Funct(parameters, defaultParameters, statements))
            program.addFunction(name, functionDeclaration)
            return functionDeclaration
        }

        val CODEC: MapCodec<FunctionDeclaration> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf("name").forGetter(FunctionDeclaration::name),
                Funct.CODEC.fieldOf("function").forGetter(FunctionDeclaration::function)
            ).apply(instance, ::FunctionDeclaration)
        }
    }
}
