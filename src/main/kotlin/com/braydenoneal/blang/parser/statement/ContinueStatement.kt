package com.braydenoneal.blang.parser.statement

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec

class ContinueStatement : Statement {
    override fun execute(program: Program): Statement {
        return this
    }

    override val type: StatementType<*> get() = StatementTypes.CONTINUE_STATEMENT

    companion object {
        fun parse(program: Program): Statement {
            program.expect(Type.KEYWORD, "continue")
            program.expect(Type.SEMICOLON)
            return ContinueStatement()
        }

        val CODEC: MapCodec<ContinueStatement> = Codec.unit(ContinueStatement()).fieldOf("continue_statement")
    }
}
