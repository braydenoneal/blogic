package com.braydenoneal.blang.parser.expression

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.builtin.list.*
import com.braydenoneal.blang.parser.expression.value.ListValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class MemberCallExpression(
    val member: Expression,
    val functionName: String,
    val arguments: Arguments
) : Expression {
    override fun evaluate(program: Program): Value<*> {
        var value: Value<*>?

        if (member is VariableExpression) {
            for (importStatement in program.imports()) {
                if (importStatement.identifiers.last() == member.name) {
                    var importProgram = program

                    for (importName in importStatement.identifiers) {
                        for (controller in importProgram.context().entity!!.getConnectedControllerBlockEntities()) {
                            if (controller.program().name() == importName) {
                                importProgram = controller.program()
                            }
                        }
                    }

                    val callExpression = CallExpression(functionName, arguments)
                    return callExpression.call(program, importProgram)
                }
            }

            value = program.scope.get(member.name)
        } else {
            value = member.evaluate(program)
        }

        if (value is ListValue) {
            when (functionName) {
                "append" -> return ListAppendBuiltin(value, arguments).evaluate(program)
                "insert" -> return ListInsertBuiltin(value, arguments).evaluate(program)
                "remove" -> return ListRemoveBuiltin(value, arguments).evaluate(program)
                "pop" -> return ListPopBuiltin(value, arguments).evaluate(program)
                "contains" -> return ListContainsBuiltin(value, arguments).evaluate(program)
                "containsAll" -> return ListContainsAllBuiltin(value, arguments).evaluate(program)
            }
        }

        throw RunException("Member is not a variable nor a list")
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.MEMBER_CALL_EXPRESSION

    companion object {
        fun parse(program: Program, member: Expression, functionName: String): Expression {
            val arguments: Arguments = Arguments.parse(program)
            return MemberCallExpression(member, functionName, arguments)
        }

        val CODEC: MapCodec<MemberCallExpression> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Expression.CODEC.fieldOf("member").forGetter(MemberCallExpression::member),
                Codec.STRING.fieldOf("functionName").forGetter(MemberCallExpression::functionName),
                Arguments.CODEC.fieldOf("arguments").forGetter(MemberCallExpression::arguments)
            ).apply(instance, ::MemberCallExpression)
        }
    }
}
