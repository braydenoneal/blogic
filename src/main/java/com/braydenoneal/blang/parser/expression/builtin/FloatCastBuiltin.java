package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.FloatValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.Value;

public record FloatCastBuiltin(Expression expression) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> value = expression.evaluate();

        if (value instanceof IntegerValue integerValue) {
            return new FloatValue((float) integerValue.value());
        }

        return null;
    }
}
