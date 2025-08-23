package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.RangeValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;

import java.util.ArrayList;
import java.util.List;

public record ForStatement(
        Program program,
        String itemName,
        Expression listExpression,
        List<Statement> statements
) implements Statement {
    @Override
    public Statement execute() {
        Value<?> listValue = listExpression.evaluate();

        if (listValue instanceof ListValue list) {
            for (Value<?> item : list.value()) {
                program.getScope().set(itemName, item);
                Statement statement = Statement.runStatements(statements);

                if (statement instanceof ReturnStatement) {
                    return statement;
                } else if (statement instanceof BreakStatement) {
                    break;
                }
            }
        } else if (listValue instanceof RangeValue range) {
            for (int i = range.value().start(); i < range.value().end(); i += range.value().step()) {
                program.getScope().set(itemName, new IntegerValue(i));
                Statement statement = Statement.runStatements(statements);

                if (statement instanceof ReturnStatement) {
                    return statement;
                } else if (statement instanceof BreakStatement) {
                    break;
                }
            }
        }

        return null;
    }

    public static Statement parse(Program program) throws Exception {
        List<Statement> statements = new ArrayList<>();

        program.expect(Type.KEYWORD, "for");
        String itemName = program.expect(Type.IDENTIFIER);
        program.expect(Type.KEYWORD, "in");
        Expression expression = Expression.parse(program);
        program.expect(Type.CURLY_BRACE, "{");

        while (!program.peekIs(Type.CURLY_BRACE, "}")) {
            statements.add(Statement.parse(program));
        }

        program.expect(Type.CURLY_BRACE, "}");

        return new ForStatement(program, itemName, expression, statements);
    }
}
