package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.Range
import com.braydenoneal.blang.parser.expression.value.RangeValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder


data class RangeBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val end = if (arguments.arguments.size == 1) arguments.integerValue(program, "end", 0).value else arguments.integerValue(program, "end", 1).value
        val start = if (arguments.arguments.size > 1) arguments.integerValue(program, "start", 0).value else 0
        val step = if (arguments.arguments.size > 2) arguments.integerValue(program, "step", 2).value else 1

        return RangeValue(Range(start, end, step))
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.RANGE_BUILTIN

    companion object {
        val CODEC: MapCodec<RangeBuiltin> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(RangeBuiltin::arguments)
            ).apply(instance, ::RangeBuiltin)
        }
    }
}
