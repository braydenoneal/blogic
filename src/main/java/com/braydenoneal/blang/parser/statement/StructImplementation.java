package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public record StructImplementation(
        String name,
        List<FunctionDeclaration> functions
) implements Statement {
    @Override
    public Value<?> execute() {
        return null;
    }
}
