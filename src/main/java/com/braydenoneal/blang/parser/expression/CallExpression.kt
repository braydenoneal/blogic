package com.braydenoneal.blang.parser.expression

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.value.FunctionValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class CallExpression(val name: String, val arguments: Arguments) : Expression {
    override fun evaluate(program: Program): Value<*> {
        return call(program, program)
    }

    fun call(program: Program, functionProgram: Program): Value<*> {
        val function = functionProgram.getFunction(name)

        if (function != null) {
            return function.call(program, arguments)
        }

        val variable = program.scope.get(name)

        if (variable is FunctionValue) {
            return variable.call(program, arguments)
        }

        throw RunException("'$name' does not refer to a function")
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.CALL_EXPRESSION

    companion object {
        val CODEC: MapCodec<CallExpression> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf("name").forGetter(CallExpression::name),
                Arguments.CODEC.fieldOf("arguments").forGetter(CallExpression::arguments)
            ).apply(instance, ::CallExpression)
        }
    }
}
