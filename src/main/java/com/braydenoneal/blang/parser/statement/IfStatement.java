package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public record IfStatement(
        Expression condition,
        List<Statement> statements
) implements Statement {
    @Override
    public Value<?> execute() {
        return null;
    }
}
