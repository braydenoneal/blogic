package com.braydenoneal.blang.parser.statement

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.value.BooleanValue
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class WhileStatement(val condition: Expression, val statements: MutableList<Statement>) : Statement {
    override fun execute(program: Program): Statement {
        val start = System.currentTimeMillis()
        var value = condition.evaluate(program)

        while (value is BooleanValue && value.value) {
            val statement: Statement? = Statement.runStatements(program, statements)

            if (statement is ReturnStatement) {
                return statement
            } else if (statement is BreakStatement) {
                break
            }

            if (System.currentTimeMillis() - start > 20) {
                throw RunException("Maximum while statement iterations exceeded")
            }

            value = condition.evaluate(program)
        }

        return this
    }

    override val type: StatementType<*> get() = StatementTypes.WHILE_STATEMENT

    companion object {
        fun parse(program: Program): Statement {
            val statements: MutableList<Statement> = ArrayList()

            program.expect(Type.KEYWORD, "while")
            val condition = Expression.parse(program)
            program.expect(Type.CURLY_BRACE, "{")

            while (!program.peekIs(Type.CURLY_BRACE, "}")) {
                statements.add(Statement.parse(program))
            }

            program.expect(Type.CURLY_BRACE, "}")

            return WhileStatement(condition, statements)
        }

        val CODEC: MapCodec<WhileStatement> = RecordCodecBuilder.mapCodec {
            it.group(
                Expression.CODEC.fieldOf("condition").forGetter(WhileStatement::condition),
                Codec.list(Statement.CODEC).fieldOf("statements").forGetter(WhileStatement::statements)
            ).apply(it, ::WhileStatement)
        }
    }
}
