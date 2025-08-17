package com.braydenoneal.blang.parser;

import com.braydenoneal.blang.parser.datatype.DataType;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.Value;

public record AssignmentStatement(
        Program program,
        String name,
        DataType type,
        String operator,
        Expression expression
) implements Statement {
    @Override
    public Value<?> execute() {
        program.getScope().set(name, expression.evaluate());
        return null;
    }
}
