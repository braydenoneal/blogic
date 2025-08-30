package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.FloatValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FloatCastBuiltin(Expression expression) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> value = expression.evaluate(program);

        if (value instanceof IntegerValue integerValue) {
            return new FloatValue((float) integerValue.value());
        }

        System.out.println("float");
        System.out.println(value);
        return null;
    }

    public static final MapCodec<FloatCastBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Expression.CODEC.fieldOf("expression").forGetter(FloatCastBuiltin::expression)
    ).apply(instance, FloatCastBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.FLOAT_CAST_BUILTIN;
    }
}
