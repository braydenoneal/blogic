package com.braydenoneal.blang.parser.expression

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.builtin.BuiltinExpression
import com.braydenoneal.blang.parser.expression.value.Value
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class VariableExpression(val name: String) : Expression {
    override fun evaluate(program: Program): Value<*> {
        val value: Value<*>? = program.scope.get(name)

        if (value == null) {
            throw RunException("Variable with name '$name' does not exist")
        }

        return value
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.VARIABLE_EXPRESSION

    companion object {
        val CODEC: MapCodec<VariableExpression> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf("name").forGetter(VariableExpression::name)
            ).apply(instance, ::VariableExpression)
        }

        fun parse(program: Program): Expression {
            val token = program.next()

            if (program.peekIs(Type.PARENTHESIS, "(")) {
                return BuiltinExpression.parse(program, token.value)
            }

            return VariableExpression(token.value)
        }
    }
}
