package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.tokenizer.Type;

public record ExpressionStatement(
        Expression expression
) implements Statement {
    @Override
    public Statement execute() {
        expression.evaluate();
        return this;
    }

    public static Statement parse(Program program) throws Exception {
        Expression expression = Expression.parse(program);
        program.expect(Type.SEMICOLON);
        return new ExpressionStatement(expression);
    }
}
