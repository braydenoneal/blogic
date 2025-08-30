package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record VariableExpression(String name) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> value = program.getScope().get(name);

        if (value == null) {
            throw new RunException("Variable with name '" + name + "' does not exist");
        }

        return value;
    }

    public static final MapCodec<VariableExpression> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(VariableExpression::name)
    ).apply(instance, VariableExpression::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.VARIABLE_EXPRESSION;
    }
}
