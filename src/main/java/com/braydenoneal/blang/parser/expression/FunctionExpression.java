package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.parser.statement.ReturnStatement;
import com.braydenoneal.blang.parser.statement.Statement;
import com.braydenoneal.blang.tokenizer.Type;

import java.util.ArrayList;
import java.util.List;

public record FunctionExpression(
        Program program,
        List<String> arguments,
        List<Statement> statements
) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> returnValue = null;
        Statement statement = Statement.runStatements(statements);

        if (statement instanceof ReturnStatement returnStatement) {
            returnValue = returnStatement.returnValue();
        }

        return returnValue;
    }

    public static Expression parse(Program program) throws Exception {
        List<String> arguments = new ArrayList<>();
        List<Statement> statements = new ArrayList<>();

        program.expect(Type.PIPE);

        while (program.peek().type() != Type.PIPE) {
            arguments.add(program.expect(Type.IDENTIFIER));

            if (program.peek().type() != Type.PIPE) {
                program.expect(Type.COMMA);
            }
        }

        program.expect(Type.PIPE);
        program.expect(Type.CURLY_BRACE, "{");

        while (!program.peekIs(Type.CURLY_BRACE, "}")) {
            statements.add(Statement.parse(program));
        }

        program.expect(Type.CURLY_BRACE, "}");
        return new FunctionExpression(program, arguments, statements);
    }
}
