package com.braydenoneal.blang.parser.expression.value

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Expression
import com.mojang.serialization.Codec

class Null {
    companion object {
        val CODEC: Codec<Null> = Codec.unit<Null>(Null())
        val VALUE = NullValue(Null())

        fun parse(program: Program): Expression {
            program.next()
            return VALUE
        }
    }
}
