package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.value.Value;

public record VariableExpression(String name) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        return program.getScope().get(name);
    }
}
