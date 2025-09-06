package com.braydenoneal.blang.parser.expression

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.expression.value.Null
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class MemberExpression(val member: Expression, val property: String) : Expression {
    override fun evaluate(program: Program): Value<*> {
        return Null.VALUE
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.MEMBER_EXPRESSION

    companion object {
        val CODEC: MapCodec<MemberExpression> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Expression.CODEC.fieldOf("member").forGetter(MemberExpression::member),
                Codec.STRING.fieldOf("property").forGetter(MemberExpression::property)
            ).apply(instance, ::MemberExpression)
        }
    }
}
