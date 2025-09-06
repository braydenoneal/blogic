package com.braydenoneal.blang.parser.expression.builtin

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.Arguments
import com.braydenoneal.blang.parser.expression.Expression
import com.braydenoneal.blang.parser.expression.ExpressionType
import com.braydenoneal.blang.parser.expression.ExpressionTypes
import com.braydenoneal.blang.parser.expression.value.StringValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder


data class StringCastBuiltin(val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        return StringValue(arguments.anyValue(program, "value", 0).value.toString())
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.STRING_CAST_BUILTIN

    companion object {
        val CODEC: MapCodec<StringCastBuiltin> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Arguments.CODEC.fieldOf("arguments").forGetter(StringCastBuiltin::arguments)
            ).apply(instance, ::StringCastBuiltin)
        }
    }
}
