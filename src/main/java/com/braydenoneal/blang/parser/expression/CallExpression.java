package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.value.FunctionValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.parser.statement.FunctionDeclaration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CallExpression(String name, Arguments arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        return call(program, program);
    }

    public Value<?> call(Program program, Program functionProgram) {
        FunctionDeclaration function = functionProgram.getFunction(name);

        if (function != null) {
            program.newScope();

            for (int i = 0; i < arguments.arguments().size(); i++) {
                program.getScope().set(function.arguments().get(i), arguments.arguments().get(i).evaluate(program));
            }

            Value<?> returnValue = function.call(program);
            program.endScope();
            return returnValue;
        }

        Value<?> variable = program.getScope().get(name);

        if (variable instanceof FunctionValue functionValue) {
            program.newScope();

            for (int i = 0; i < Math.min(arguments.arguments().size(), functionValue.value().arguments().size()); i++) {
                program.getScope().set(functionValue.value().arguments().get(i), arguments.arguments().get(i).evaluate(program));
            }

            Value<?> returnValue = functionValue.call(program);
            program.endScope();
            return returnValue;
        }

        throw new RunException("'" + name + "' does not refer to a function");
    }

    public static final MapCodec<CallExpression> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(CallExpression::name),
            Arguments.CODEC.fieldOf("arguments").forGetter(CallExpression::arguments)
    ).apply(instance, CallExpression::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.CALL_EXPRESSION;
    }
}
