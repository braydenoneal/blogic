package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public record StructDeclaration(
        String name,
        List<String> members
) implements Statement {
    @Override
    public Value<?> execute() {
        return null;
    }
}
