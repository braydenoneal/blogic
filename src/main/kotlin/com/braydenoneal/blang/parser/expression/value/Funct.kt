package com.braydenoneal.blang.parser.expression.value

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.statement.ReturnStatement
import com.braydenoneal.blang.parser.statement.Statement
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.function.Consumer

data class Funct(
    val parameters: MutableList<String>,
    val defaultParameters: MutableList<Pair<String, Expression>>,
    val statements: MutableList<Statement>
) {
    fun call(program: Program, arguments: Arguments): Value<*> {
        program.newScope()

        arguments.namedArguments.forEach { name: String, expression: Expression ->
            val hasDefault = this.defaultParameters.stream().anyMatch { it.first == name }
            if (parameters.contains(name) || hasDefault) {
                program.scope.setLocal(name, expression.evaluate(program))
            } else {
                throw RunException("Provided extra argument '$name'")
            }
        }

        for (i in parameters.indices) {
            if (program.scope.getLocal(parameters[i]) == null) {
                if (arguments.arguments.size > i) {
                    program.scope.setLocal(
                        parameters[i],
                        arguments.arguments[i].evaluate(program)
                    )
                } else {
                    throw RunException("Missing argument '" + parameters[i] + "'")
                }
            }
        }

        for (i in parameters.size..<arguments.arguments.size) {
            if (defaultParameters.size > i - parameters.size) {
                program.scope.setLocal(
                    defaultParameters[i - parameters.size].first,
                    arguments.arguments[i].evaluate(program)
                )
            } else {
                throw RunException("Provided extra argument")
            }
        }

        defaultParameters.forEach(Consumer {
            if (program.scope.getLocal(it.getFirst()) == null) {
                program.scope.setLocal(it.getFirst(), it.getSecond().evaluate(program))
            }
        })

        var returnValue: Value<*> = Null.VALUE
        val statement = Statement.runStatements(program, statements)

        if (statement is ReturnStatement) {
            returnValue = statement.returnValue(program)
        }

        program.endScope()
        return returnValue
    }

    companion object {
        val CODEC: Codec<Funct> = RecordCodecBuilder.create {
            it.group(
                Codec.list(Codec.STRING).fieldOf("parameters").forGetter(Funct::parameters),
                Codec.list(Codec.pair(Codec.STRING, Expression.CODEC)).fieldOf("defaultParameters").forGetter(Funct::defaultParameters),
                Codec.list(Statement.CODEC).fieldOf("statements").forGetter(Funct::statements)
            ).apply(it, ::Funct)
        }
    }
}
