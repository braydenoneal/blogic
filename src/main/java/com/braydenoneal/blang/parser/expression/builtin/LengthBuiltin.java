package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;

public record LengthBuiltin(Expression expression) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> value = expression.evaluate();

        if (value instanceof ListValue listValue) {
            return new IntegerValue(listValue.value().size());
        }

        return null;
    }
}
