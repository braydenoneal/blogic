package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.StringValue;
import com.braydenoneal.blang.parser.expression.value.Value;

public record StringCastBuiltin(Expression expression) implements Expression {
    @Override
    public Value<?> evaluate() {
        return new StringValue(expression.evaluate().value().toString());
    }
}
