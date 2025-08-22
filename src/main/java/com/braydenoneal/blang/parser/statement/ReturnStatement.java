package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;

public record ReturnStatement(
        Expression expression
) implements Statement {
    @Override
    public Statement execute() {
        return this;
    }

    Value<?> returnValue() {
        return expression.evaluate();
    }

    public static Statement parse(Program program) throws Exception {
        program.expect(Type.KEYWORD, "return");
        Expression expression = Expression.parse(program);
        program.expect(Type.SEMICOLON);
        return new ReturnStatement(expression);
    }
}
