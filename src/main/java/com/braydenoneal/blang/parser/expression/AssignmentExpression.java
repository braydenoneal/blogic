package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;

public record AssignmentExpression(
        Program program,
        String name,
        Expression expression
) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> value = expression.evaluate();
        program.getScope().set(name, value);
        return value;
    }

    public static Expression parse(Program program, String name) throws Exception {
        program.expect(Type.ASSIGN, "=");
        Expression expression = Expression.parse(program);
        return new AssignmentExpression(program, name, expression);
    }
}
