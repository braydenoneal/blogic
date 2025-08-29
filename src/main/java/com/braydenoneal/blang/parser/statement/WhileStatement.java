package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.tokenizer.Type;

import java.util.ArrayList;
import java.util.List;

public record WhileStatement(
        Expression condition,
        List<Statement> statements
) implements Statement {
    @Override
    public Statement execute() {
        long start = System.currentTimeMillis();

        while (condition.evaluate() instanceof BooleanValue booleanValue && booleanValue.value()) {
            Statement statement = Statement.runStatements(statements);

            if (statement instanceof ReturnStatement) {
                return statement;
            } else if (statement instanceof BreakStatement) {
                break;
            }

            if (System.currentTimeMillis() - start > 15) {
                System.out.println("while statement: max time exceeded");
                return null;
            }
        }

        return null;
    }

    public static Statement parse(Program program) throws Exception {
        List<Statement> statements = new ArrayList<>();

        program.expect(Type.KEYWORD, "while");
        Expression condition = Expression.parse(program);
        program.expect(Type.CURLY_BRACE, "{");

        while (!program.peekIs(Type.CURLY_BRACE, "}")) {
            statements.add(Statement.parse(program));
        }

        program.expect(Type.CURLY_BRACE, "}");

        return new WhileStatement(condition, statements);
    }
}
