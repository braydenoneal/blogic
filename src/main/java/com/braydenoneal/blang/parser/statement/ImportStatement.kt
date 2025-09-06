package com.braydenoneal.blang.parser.statement

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class ImportStatement(val identifiers: MutableList<String>) : Statement {
    override fun execute(program: Program): Statement {
        program.addImport(this)
        return this
    }

    override val type: StatementType<*> get() = StatementTypes.IMPORT_STATEMENT

    companion object {
        fun parse(program: Program): Statement {
            val identifiers: MutableList<String> = ArrayList()
            program.expect(Type.KEYWORD, "import")

            while (program.peek().type == Type.IDENTIFIER) {
                identifiers.add(program.next().value)

                if (program.peek().type != Type.SEMICOLON) {
                    program.expect(Type.DOT)
                }
            }

            program.expect(Type.SEMICOLON)
            return ImportStatement(identifiers)
        }

        val CODEC: MapCodec<ImportStatement> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Codec.list(Codec.STRING).fieldOf("identifiers").forGetter(ImportStatement::identifiers)
                ).apply(instance, ::ImportStatement)
            }
    }
}
