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
import kotlin.math.ceil


data class CeilBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        return FloatValue(ceil(arguments.floatValue(program, "value", 0).value.toDouble()).toFloat())
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.ABSOLUTE_VALUE_BUILTIN

    companion object {
        val CODEC: MapCodec<CeilBuiltin> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(CeilBuiltin::arguments)
            ).apply(instance, ::CeilBuiltin)
        }
    }
}
