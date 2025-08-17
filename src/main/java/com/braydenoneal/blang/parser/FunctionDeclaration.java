package com.braydenoneal.blang.parser;

import com.braydenoneal.blang.parser.datatype.DataType;
import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;
import java.util.Map;

public record FunctionDeclaration(
        Program program,
        DataType returnType,
        Map<String, DataType> arguments,
        List<Statement> statements
) implements Statement {
    @Override
    public Value<?> execute() {
        Value<?> returnValue = null;

        for (Statement statement : statements) {
            returnValue = statement.execute();

            if (statement instanceof ReturnStatement) {
                return returnValue;
            }
        }

        return returnValue;
    }
}
