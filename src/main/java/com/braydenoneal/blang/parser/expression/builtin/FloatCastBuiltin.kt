package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.FloatValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder


data class FloatCastBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        return FloatValue(arguments.integerValue(program, "value", 0).value().toFloat())
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.FLOAT_CAST_BUILTIN

    companion object {
        val CODEC: MapCodec<FloatCastBuiltin> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(FloatCastBuiltin::arguments)
            ).apply(instance, ::FloatCastBuiltin)
        }
    }
}
