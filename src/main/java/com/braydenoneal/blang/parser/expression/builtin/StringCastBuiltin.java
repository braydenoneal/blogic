package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.StringValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record StringCastBuiltin(Expression expression) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        return new StringValue(expression.evaluate(program).value().toString());
    }

    public static final MapCodec<StringCastBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Expression.CODEC.fieldOf("expression").forGetter(StringCastBuiltin::expression)
    ).apply(instance, StringCastBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.STRING_CAST_BUILTIN;
    }
}
