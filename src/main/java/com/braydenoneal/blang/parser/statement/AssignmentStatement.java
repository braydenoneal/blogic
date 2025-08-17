package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.Value;

public record AssignmentStatement(
        Program program,
        String name,
        Expression expression
) implements Statement {
    @Override
    public Value<?> execute() {
        program.getScope().set(name, expression.evaluate());
        return null;
    }
}
