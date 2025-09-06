package com.braydenoneal.blang.parser.statement

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec

class BreakStatement : Statement {
    override fun execute(program: Program): Statement {
        return this
    }

    override val type: StatementType<*> get() = StatementTypes.BREAK_STATEMENT

    companion object {
        fun parse(program: Program): Statement {
            program.expect(Type.KEYWORD, "break")
            program.expect(Type.SEMICOLON)
            return BreakStatement()
        }

        val CODEC: MapCodec<BreakStatement> = Codec.unit(BreakStatement()).fieldOf("break_statement")
    }
}
