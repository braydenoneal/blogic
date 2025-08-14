package com.braydenoneal.blang.parser;

import com.braydenoneal.blang.parser.datatype.DataType;

import java.util.List;
import java.util.Map;

public record FunctionDeclaration(
        String name,
        DataType returnType,
        Map<String, DataType> arguments,
        List<Statement> statements
) implements Statement {
}
