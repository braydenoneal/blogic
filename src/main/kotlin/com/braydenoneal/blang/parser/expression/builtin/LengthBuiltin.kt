package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.IntegerValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder


data class LengthBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        return IntegerValue(arguments.listValue(program, "value", 0).value.size)
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.LENGTH_BUILTIN

    companion object {
        val CODEC: MapCodec<LengthBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(LengthBuiltin::arguments)
            ).apply(it, ::LengthBuiltin)
        }
    }
}
