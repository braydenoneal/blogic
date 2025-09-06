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
import kotlin.math.min


data class MinimumBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        var a = arguments.anyValue(program, "a", 0)
        var b = arguments.anyValue(program, "b", 1)

        if (a is IntegerValue && b is FloatValue) {
            a = FloatValue(a.value().toFloat())
        } else if (a is FloatValue && b is IntegerValue) {
            b = FloatValue(b.value().toFloat())
        }

        if (a is IntegerValue && b is IntegerValue) {
            return IntegerValue(min(a.value(), b.value()))
        } else if (a is FloatValue && b is FloatValue) {
            return FloatValue(min(a.value(), b.value()))
        }

        throw RunException("Arguments are not numbers")
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.MINIMUM_BUILTIN

    companion object {
        val CODEC: MapCodec<MinimumBuiltin> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(MinimumBuiltin::arguments)
            ).apply(instance, ::MinimumBuiltin)
        }
    }
}
