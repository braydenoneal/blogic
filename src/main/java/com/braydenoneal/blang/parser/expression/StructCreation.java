package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public record StructCreation(Expression struct, List<Expression> values) implements Expression {
    @Override
    public Value<?> evaluate() {
        return null;
    }
}
