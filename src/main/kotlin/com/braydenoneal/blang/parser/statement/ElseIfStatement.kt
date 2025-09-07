package com.braydenoneal.blang.parser.statement

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class ElseIfStatement(val condition: Expression, val statements: MutableList<Statement>) {
    companion object {
        fun parse(program: Program): ElseIfStatement {
            val statements: MutableList<Statement> = ArrayList()

            program.expect(Type.KEYWORD, "elif")
            val condition = Expression.parse(program)
            program.expect(Type.CURLY_BRACE, "{")

            while (!program.peekIs(Type.CURLY_BRACE, "}")) {
                statements.add(Statement.parse(program))
            }

            program.expect(Type.CURLY_BRACE, "}")

            return ElseIfStatement(condition, statements)
        }

        val CODEC: Codec<ElseIfStatement> = RecordCodecBuilder.create {
            it.group(
                Expression.CODEC.fieldOf("condition").forGetter(ElseIfStatement::condition),
                Codec.list(Statement.CODEC).fieldOf("statements").forGetter(ElseIfStatement::statements)
            ).apply(it, ::ElseIfStatement)
        }
    }
}
