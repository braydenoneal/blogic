package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record LengthBuiltin(Expression expression) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> value = expression.evaluate(program);

        if (value instanceof ListValue listValue) {
            return new IntegerValue(listValue.value().size());
        }

        System.out.println("len");
        System.out.println(value);
        return null;
    }

    public static final MapCodec<LengthBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Expression.CODEC.fieldOf("expression").forGetter(LengthBuiltin::expression)
    ).apply(instance, LengthBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.LENGTH_BUILTIN;
    }
}
