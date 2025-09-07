package com.braydenoneal.blang.parser.statement

import com.braydenoneal.blang.parser.ParseException
import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class DeleteStatement(val name: String) : Statement {
    override fun execute(program: Program): Statement {
        program.scope.delete(name) ?: run { throw RunException("Variable with name '$name' does not exist") }
        return this
    }

    override val type: StatementType<*> get() = StatementTypes.DELETE_STATEMENT

    companion object {
        val CODEC: MapCodec<DeleteStatement> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.STRING.fieldOf("name").forGetter(DeleteStatement::name)
            ).apply(it, ::DeleteStatement)
        }

        fun parse(program: Program): Statement {
            program.expect(Type.KEYWORD, "del")

            if (program.peek().type != Type.IDENTIFIER) {
                throw ParseException("Expression is not an identifier")
            }

            val name = program.next().value
            program.expect(Type.SEMICOLON)
            return DeleteStatement(name)
        }
    }
}
