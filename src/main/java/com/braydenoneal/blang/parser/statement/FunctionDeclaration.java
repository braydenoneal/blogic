package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;

import java.util.ArrayList;
import java.util.List;

public record FunctionDeclaration(
        Program program,
        String name,
        List<String> arguments,
        List<Statement> statements
) implements Statement {
    @Override
    public Value<?> execute() {
        return null;
    }

    public Value<?> call() {
        Value<?> returnValue = null;
        program.newScope();

        for (Statement statement : statements) {
            returnValue = statement.execute();

            if (statement instanceof ReturnStatement) {
                return returnValue;
            }
        }

        program.endScope();
        return returnValue;
    }

    public static Statement parse(Program program) throws Exception {
        List<String> arguments = new ArrayList<>();
        List<Statement> statements = new ArrayList<>();

        program.expect(Type.KEYWORD, "fn");
        String name = program.expect(Type.IDENTIFIER);
        program.expect(Type.PARENTHESIS, "(");

        while (!program.peekIs(Type.PARENTHESIS, ")")) {
            arguments.add(program.expect(Type.IDENTIFIER));

            if (!program.peekIs(Type.PARENTHESIS, ")")) {
                program.expect(Type.COMMA);
            }
        }

        program.expect(Type.PARENTHESIS, ")");
        program.expect(Type.CURLY_BRACE, "{");

        while (!program.peekIs(Type.CURLY_BRACE, "}")) {
            statements.add(Statement.parse(program));
        }

        program.expect(Type.CURLY_BRACE, "}");

        FunctionDeclaration functionDeclaration = new FunctionDeclaration(program, name, arguments, statements);
        program.addFunction(name, functionDeclaration);
        return functionDeclaration;
    }
}
