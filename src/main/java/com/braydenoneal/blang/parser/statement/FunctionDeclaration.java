package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public record FunctionDeclaration(
        Program program,
        List<String> arguments,
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

    public static Statement parse(Program program) throws Exception {
        return null;
    }
}
