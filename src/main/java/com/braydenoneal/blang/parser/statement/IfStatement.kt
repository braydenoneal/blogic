package com.braydenoneal.blang.parser.statement

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.value.BooleanValue
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class IfStatement(
    val condition: Expression,
    val statements: MutableList<Statement>,
    val elseIfStatements: MutableList<ElseIfStatement>,
    val elseStatement: ElseStatement?
) : Statement {
    override fun execute(program: Program): Statement? {
        val value = condition.evaluate(program)

        if (value is BooleanValue && value.value()) {
            return Statement.runStatements(program, statements)
        }

        for (elseIfStatement in elseIfStatements) {
            val elseIfValue = elseIfStatement.condition.evaluate(program)

            if (elseIfValue is BooleanValue && elseIfValue.value()) {
                return Statement.runStatements(program, elseIfStatement.statements)
            }
        }

        if (elseStatement == null) {
            return this
        }

        return Statement.runStatements(program, elseStatement.statements)
    }

    override val type: StatementType<*> get() = StatementTypes.IF_STATEMENT

    companion object {
        fun parse(program: Program): Statement {
            val statements: MutableList<Statement> = ArrayList()
            val elseIfStatements: MutableList<ElseIfStatement> = ArrayList()
            var elseStatement: ElseStatement? = null

            program.expect(Type.KEYWORD, "if")
            val condition = Expression.parse(program)
            program.expect(Type.CURLY_BRACE, "{")

            while (!program.peekIs(Type.CURLY_BRACE, "}")) {
                statements.add(Statement.parse(program))
            }

            program.expect(Type.CURLY_BRACE, "}")

            while (program.peekIs(Type.KEYWORD, "elif")) {
                elseIfStatements.add(ElseIfStatement.parse(program))
            }

            if (program.peekIs(Type.KEYWORD, "else")) {
                elseStatement = ElseStatement.parse(program)
            }

            return IfStatement(condition, statements, elseIfStatements, elseStatement)
        }

        val CODEC: MapCodec<IfStatement> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Expression.CODEC.fieldOf("condition").forGetter(IfStatement::condition),
                Codec.list(Statement.CODEC).fieldOf("statements").forGetter(IfStatement::statements),
                Codec.list(ElseIfStatement.CODEC).fieldOf("elseIfStatements").forGetter(IfStatement::elseIfStatements),
                ElseStatement.CODEC.fieldOf("ElseStatement").forGetter(IfStatement::elseStatement)
            ).apply(instance, ::IfStatement)
        }
    }
}

