package com.braydenoneal.blang.parser.expression

import com.braydenoneal.blang.parser.Program
import com.braydenoneal.blang.parser.RunException
import com.braydenoneal.blang.parser.expression.builtin.list.*
import com.braydenoneal.blang.parser.expression.value.ListValue
import com.braydenoneal.blang.parser.expression.value.Value
import com.braydenoneal.blang.tokenizer.Type
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class MemberCallExpression(
    val member: Expression,
    val functionName: String,
    val arguments: Arguments
) : Expression {
    override fun evaluate(program: Program): Value<*> {
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

            val item = program.scope.get(member.name)

            if (item is ListValue) {
                when (functionName) {
                    "append" -> return ListAppendBuiltin(member.name, item, arguments).evaluate(program)
                    "insert" -> return ListInsertBuiltin(member.name, item, arguments).evaluate(program)
                    "remove" -> return ListRemoveBuiltin(member.name, item, arguments).evaluate(program)
                    "pop" -> return ListPopBuiltin(member.name, item, arguments).evaluate(program)
                    "contains" -> return ListContainsBuiltin(item, arguments).evaluate(program)
                    "containsAll" -> return ListContainsAllBuiltin(item, arguments).evaluate(program)
                }
            }
        } else {
            val value = member.evaluate(program)

            if (value is ListValue) {
                when (functionName) {
                    "contains" -> return ListContainsBuiltin(value, arguments).evaluate(program)
                    "containsAll" -> return ListContainsAllBuiltin(value, arguments).evaluate(program)
                }
            }
        }

        throw RunException("Member is not a variable nor a list")
    }

    override val type: ExpressionType<*> get() = ExpressionTypes.MEMBER_CALL_EXPRESSION

    companion object {
        fun parse(program: Program, member: Expression): Expression {
            program.expect(Type.DOT)
            val functionName = program.expect(Type.IDENTIFIER)
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
