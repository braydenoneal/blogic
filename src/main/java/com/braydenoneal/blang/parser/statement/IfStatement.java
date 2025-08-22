package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record IfStatement(
        Expression condition,
        List<Statement> statements,
        List<ElseIfStatement> elseIfStatements,
        @Nullable ElseStatement elseStatement
) implements Statement {
    @Override
    public Statement execute() {
        Value<?> value = condition.evaluate();

        if (value instanceof BooleanValue booleanValue && booleanValue.value()) {
            return Statement.runStatements(statements);
        }

        for (ElseIfStatement elseIfStatement : elseIfStatements) {
            Value<?> elseIfValue = elseIfStatement.condition().evaluate();

            if (elseIfValue instanceof BooleanValue booleanValue && booleanValue.value()) {
                return Statement.runStatements(elseIfStatement.statements);
            }
        }

        if (elseStatement == null) {
            return null;
        }

        return Statement.runStatements(elseStatement.statements);
    }

    public static Statement parse(Program program) throws Exception {
        List<Statement> statements = new ArrayList<>();
        List<ElseIfStatement> elseIfStatements = new ArrayList<>();
        ElseStatement elseStatement = null;

        program.expect(Type.KEYWORD, "if");
        Expression condition = Expression.parse(program);
        program.expect(Type.CURLY_BRACE, "{");

        while (!program.peekIs(Type.CURLY_BRACE, "}")) {
            statements.add(Statement.parse(program));
        }

        program.expect(Type.CURLY_BRACE, "}");

        while (program.peekIs(Type.KEYWORD, "elif")) {
            elseIfStatements.add(ElseIfStatement.parse(program));
        }

        if (program.peekIs(Type.KEYWORD, "else")) {
            elseStatement = ElseStatement.parse(program);
        }

        return new IfStatement(condition, statements, elseIfStatements, elseStatement);
    }

    record ElseIfStatement(Expression condition, List<Statement> statements) {
        public static ElseIfStatement parse(Program program) throws Exception {
            List<Statement> statements = new ArrayList<>();

            program.expect(Type.KEYWORD, "elif");
            Expression condition = Expression.parse(program);
            program.expect(Type.CURLY_BRACE, "{");

            while (!program.peekIs(Type.CURLY_BRACE, "}")) {
                statements.add(Statement.parse(program));
            }

            program.expect(Type.CURLY_BRACE, "}");

            return new ElseIfStatement(condition, statements);
        }
    }

    record ElseStatement(List<Statement> statements) {
        public static ElseStatement parse(Program program) throws Exception {
            List<Statement> statements = new ArrayList<>();

            program.expect(Type.KEYWORD, "else");
            program.expect(Type.CURLY_BRACE, "{");

            while (!program.peekIs(Type.CURLY_BRACE, "}")) {
                statements.add(Statement.parse(program));
            }

            program.expect(Type.CURLY_BRACE, "}");

            return new ElseStatement(statements);
        }
    }
}
