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
            return function.call(program, arguments);
        }

        Value<?> variable = program.getScope().get(name);

        if (variable instanceof FunctionValue functionValue) {
            return functionValue.call(program, arguments);
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
