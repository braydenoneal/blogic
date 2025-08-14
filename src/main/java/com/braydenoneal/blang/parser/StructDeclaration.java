package com.braydenoneal.blang.parser;

import com.braydenoneal.blang.parser.datatype.DataType;

import java.util.Map;

public record StructDeclaration(
        String name,
        Map<String, DataType> members
) implements Statement {
}
