package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record MemberExpression(Expression object, String property) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        return null;
    }

    public static final MapCodec<MemberExpression> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Expression.CODEC.fieldOf("object").forGetter(MemberExpression::object),
            Codec.STRING.fieldOf("property").forGetter(MemberExpression::property)
    ).apply(instance, MemberExpression::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.MEMBER_EXPRESSION;
    }
}
