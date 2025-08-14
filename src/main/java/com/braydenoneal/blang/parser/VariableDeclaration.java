package com.braydenoneal.blang.parser;

import com.braydenoneal.blang.parser.datatype.DataType;
import com.braydenoneal.blang.parser.expression.Expression;

public record VariableDeclaration(
        String name,
        DataType type,
        int listLevel,
        Expression expression
) implements Statement {
}
