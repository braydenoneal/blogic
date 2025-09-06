package com.braydenoneal.blang.parser.statement

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.value.IntegerValue
import com.braydenoneal.blang.parser.expression.value.ListValue
import com.braydenoneal.blang.parser.expression.value.RangeValue
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class ForStatement(
    val itemName: String,
    val listExpression: Expression,
    val statements: MutableList<Statement>
) : Statement {
    override fun execute(program: Program): Statement {
        val value = listExpression.evaluate(program)

        if (value is ListValue) {
            for (item in value.value()) {
                program.scope.set(itemName, item)
                val statement = Statement.runStatements(program, statements)

                if (statement is ReturnStatement) {
                    return statement
                } else if (statement is BreakStatement) {
                    break
                }
            }
        } else if (value is RangeValue) {
            for (i in value.value().start..<value.value().end step value.value().step) {
                program.scope.set(itemName, IntegerValue(i))
                val statement = Statement.runStatements(program, statements)

                if (statement is ReturnStatement) {
                    return statement
                } else if (statement is BreakStatement) {
                    break
                }
            }
        }

        return this
    }

    override val type: StatementType<*> get() = StatementTypes.FOR_STATEMENT

    companion object {
        fun parse(program: Program): Statement {
            val statements: MutableList<Statement> = ArrayList()

            program.expect(Type.KEYWORD, "for")
            val itemName = program.expect(Type.IDENTIFIER)
            program.expect(Type.KEYWORD, "in")
            val expression = Expression.parse(program)
            program.expect(Type.CURLY_BRACE, "{")

            while (!program.peekIs(Type.CURLY_BRACE, "}")) {
                statements.add(Statement.parse(program))
            }

            program.expect(Type.CURLY_BRACE, "}")
            return ForStatement(itemName, expression, statements)
        }

        val CODEC: MapCodec<ForStatement> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf("itemName").forGetter(ForStatement::itemName),
                Expression.CODEC.fieldOf("listExpression").forGetter(ForStatement::listExpression),
                Codec.list(Statement.CODEC).fieldOf("statements").forGetter(ForStatement::statements)
            ).apply(instance, ::ForStatement)
        }
    }
}
