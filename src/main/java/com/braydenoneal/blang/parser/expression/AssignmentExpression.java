package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.operator.ArithmeticOperator;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;

public record AssignmentExpression(
        Program program,
        String name,
        String type,
        Expression expression
) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> value = expression.evaluate();

        if (type.equals("=")) {
            return program.getScope().set(name, value);
        }

        Value<?> prev = program.getScope().get(name);
        return program.getScope().set(name, new ArithmeticOperator(type.equals("+=") ? "+" : "-", prev, value).evaluate());
    }

    public static Expression parse(Program program, String name) throws Exception {
        String type = program.expect(Type.ASSIGN);
        Expression expression = Expression.parse(program);
        return new AssignmentExpression(program, name, type, expression);
    }
}
