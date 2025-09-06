package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.FloatValue
import com.braydenoneal.blang.parser.expression.value.IntegerValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlin.math.abs


data class AbsoluteValueBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val value = arguments.anyValue(program, "value", 0)

        if (value is IntegerValue) {
            return IntegerValue(abs(value.value()))
        } else if (value is FloatValue) {
            return FloatValue(abs(value.value()))
        }

        throw RunException("Expression is not a number")
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.ABSOLUTE_VALUE_BUILTIN

    companion object {
        val CODEC: MapCodec<AbsoluteValueBuiltin> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(AbsoluteValueBuiltin::arguments)
            ).apply(instance, ::AbsoluteValueBuiltin)
        }
    }
}
