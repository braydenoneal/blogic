package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.Range;
import com.braydenoneal.blang.parser.expression.value.RangeValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record RangeBuiltin(List<Expression> arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        int start = 0;
        int end = 0;
        int step = 1;

        if (arguments.size() == 1) {
            Value<?> endValue = arguments.getFirst().evaluate(program);

            if (endValue instanceof IntegerValue integerValue) {
                end = integerValue.value();
            }
        } else if (arguments.size() > 1) {
            Value<?> startValue = arguments.getFirst().evaluate(program);
            Value<?> endValue = arguments.get(1).evaluate(program);

            if (startValue instanceof IntegerValue startInt && endValue instanceof IntegerValue endInt) {
                start = startInt.value();
                end = endInt.value();
            }
        }

        if (arguments.size() == 3) {
            Value<?> stepValue = arguments.get(2).evaluate(program);

            if (stepValue instanceof IntegerValue integerValue) {
                step = integerValue.value();
            }
        }

        return new RangeValue(new Range(start, end, step));
    }

    public static final MapCodec<RangeBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.list(Expression.CODEC).fieldOf("arguments").forGetter(RangeBuiltin::arguments)
    ).apply(instance, RangeBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.RANGE_BUILTIN;
    }
}
