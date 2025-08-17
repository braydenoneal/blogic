package com.braydenoneal.blang.parser;

import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.Value;

public record ReturnStatement(
        Expression expression
) implements Statement {
    @Override
    public Value<?> execute() {
        return expression.evaluate();
    }
}
