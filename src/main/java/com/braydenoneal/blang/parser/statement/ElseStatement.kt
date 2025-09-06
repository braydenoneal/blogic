package com.braydenoneal.blang.parser.statement

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class ElseStatement(val statements: MutableList<Statement>) {
    companion object {
        fun parse(program: Program): ElseStatement {
            val statements: MutableList<Statement> = ArrayList()

            program.expect(Type.KEYWORD, "else")
            program.expect(Type.CURLY_BRACE, "{")

            while (!program.peekIs(Type.CURLY_BRACE, "}")) {
                statements.add(Statement.parse(program))
            }

            program.expect(Type.CURLY_BRACE, "}")

            return ElseStatement(statements)
        }

        val CODEC: MapCodec<ElseStatement> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.list(Statement.CODEC).fieldOf("statements").forGetter(ElseStatement::statements)
            ).apply(instance, ::ElseStatement)
        }
    }
}
