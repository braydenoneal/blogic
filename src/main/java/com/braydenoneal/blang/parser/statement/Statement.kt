package com.braydenoneal.blang.parser.statement

import com.braydenoneal.blang.parser.ParseException
import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.Codec

interface Statement {
    fun execute(program: Program): Statement?

    val type: StatementType<*>

    companion object {
        fun parse(program: Program): Statement {
            val token = program.peek()

            if (token.type == Type.KEYWORD) {
                when (token.value) {
                    "import" -> {
                        return ImportStatement.parse(program)
                    }

                    "fn" -> {
                        return FunctionDeclaration.parse(program)
                    }

                    "if" -> {
                        return IfStatement.parse(program)
                    }

                    "while" -> {
                        return WhileStatement.parse(program)
                    }

                    "for" -> {
                        return ForStatement.parse(program)
                    }

                    "break" -> {
                        return BreakStatement.parse(program)
                    }

                    "continue" -> {
                        return ContinueStatement.parse(program)
                    }

                    "return" -> {
                        return ReturnStatement.parse(program)
                    }
                }
            } else {
                return ExpressionStatement.parse(program)
            }

            throw ParseException("Unrecognized statement at $token")
        }

        fun runStatements(program: Program, statements: MutableList<Statement>): Statement? {
            for (statement in statements) {
                val statementResult = statement.execute(program)

                if (statementResult is ReturnStatement ||
                    statementResult is BreakStatement ||
                    statementResult is ContinueStatement
                ) {
                    return statementResult
                }
            }

            return null
        }

        val CODEC: Codec<Statement> = StatementType.REGISTRY.getCodec().dispatch("type", Statement::type, StatementType<*>::codec)
    }
}
