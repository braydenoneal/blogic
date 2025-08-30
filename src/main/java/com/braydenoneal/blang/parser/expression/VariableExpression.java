package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record VariableExpression(String name) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        return program.getScope().get(name);
    }

    public static final MapCodec<VariableExpression> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(VariableExpression::name)
    ).apply(instance, VariableExpression::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.VARIABLE_EXPRESSION;
    }
}
