package com.braydenoneal.blang.parser;

import java.util.List;

public record StructImplementation(
        String name,
        List<FunctionDeclaration> functions
) implements Statement {
}
