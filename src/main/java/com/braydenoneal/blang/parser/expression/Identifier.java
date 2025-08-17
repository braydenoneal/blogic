package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public record Identifier(List<String> location, String name) implements Expression {
    @Override
    public Value<?> evaluate() {
        return null;
    }
}
