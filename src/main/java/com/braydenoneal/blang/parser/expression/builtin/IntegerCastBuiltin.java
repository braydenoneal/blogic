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

public record IntegerCastBuiltin(Expression expression) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> value = expression.evaluate(program);

        if (value instanceof FloatValue floatValue) {
            return new IntegerValue(floatValue.value().intValue());
        }

        System.out.println("int");
        System.out.println(value);
        return null;
    }

    public static final MapCodec<IntegerCastBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Expression.CODEC.fieldOf("expression").forGetter(IntegerCastBuiltin::expression)
    ).apply(instance, IntegerCastBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.INTEGER_CAST_BUILTIN;
    }
}
