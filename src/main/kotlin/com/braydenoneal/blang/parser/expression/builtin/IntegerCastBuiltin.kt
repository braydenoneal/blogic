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


data class IntegerCastBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        return IntegerValue(this.arguments.floatValue(program, "value", 0).value.toInt())
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.INTEGER_CAST_BUILTIN

    companion object {
        val CODEC: MapCodec<IntegerCastBuiltin> = RecordCodecBuilder.mapCodec {
            it.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(IntegerCastBuiltin::arguments)
            ).apply(it, ::IntegerCastBuiltin)
        }
    }
}
