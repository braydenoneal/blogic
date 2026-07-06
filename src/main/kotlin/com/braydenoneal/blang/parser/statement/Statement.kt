package com.braydenoneal.blang.parser.statement

import com.braydenoneal.blang.parser.ParseException
import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.Codec
import java.util.function.Function

interface Statement {
    fun execute(program: Program): Statement?

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

                    "del" -> {
                        return DeleteStatement.parse(program)
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

        val getStatementType: Function<in Statement, out StatementType<*>> = { statement: Statement ->
            when (statement) {
                is BreakStatement -> StatementTypes.BREAK_STATEMENT
                is ContinueStatement -> StatementTypes.CONTINUE_STATEMENT
                is DeleteStatement -> StatementTypes.DELETE_STATEMENT
                is ExpressionStatement -> StatementTypes.EXPRESSION_STATEMENT
                is ForStatement -> StatementTypes.FOR_STATEMENT
                is FunctionDeclaration -> StatementTypes.FUNCTION_DECLARATION
                is IfStatement -> StatementTypes.IF_STATEMENT
                is ImportStatement -> StatementTypes.IMPORT_STATEMENT
                is ReturnStatement -> StatementTypes.RETURN_STATEMENT
                is WhileStatement -> StatementTypes.WHILE_STATEMENT
                else -> throw Exception("Statement type not found")
            }
        }

        val STATEMENT_CODEC: Codec<Statement> = StatementType.REGISTRY.getCodec().dispatch("type", getStatementType, StatementType<*>::codec)
    }
}
