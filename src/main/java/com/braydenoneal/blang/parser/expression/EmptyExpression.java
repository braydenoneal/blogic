package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.expression.value.Value;

public record EmptyExpression() implements Expression {
    @Override
    public Value<?> evaluate() {
        return null;
    }
}
