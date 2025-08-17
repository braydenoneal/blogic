package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.expression.value.Value;

public record MemberExpression(Expression object, String property) implements Expression {
    @Override
    public Value<?> evaluate() {
        return null;
    }
}
