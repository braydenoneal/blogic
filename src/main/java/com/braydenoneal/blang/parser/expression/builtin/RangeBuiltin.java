package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.Range;
import com.braydenoneal.blang.parser.expression.value.RangeValue;
import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public record RangeBuiltin(List<Expression> arguments) implements Expression {
    @Override
    public Value<?> evaluate() {
        int start = 0;
        int end = 0;
        int step = 1;

        if (arguments.size() == 1) {
            Value<?> endValue = arguments.getFirst().evaluate();

            if (endValue instanceof IntegerValue integerValue) {
                end = integerValue.value();
            }
        } else if (arguments.size() > 1) {
            Value<?> startValue = arguments.getFirst().evaluate();
            Value<?> endValue = arguments.get(1).evaluate();

            if (startValue instanceof IntegerValue startInt && endValue instanceof IntegerValue endInt) {
                start = startInt.value();
                end = endInt.value();
            }
        }

        if (arguments.size() == 3) {
            Value<?> stepValue = arguments.get(2).evaluate();

            if (stepValue instanceof IntegerValue integerValue) {
                step = integerValue.value();
            }
        }

        return new RangeValue(new Range(start, end, step));
    }
}
