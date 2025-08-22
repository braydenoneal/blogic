package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.operator.ArithmeticOperator;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;

import java.util.ArrayList;
import java.util.List;

public record AssignmentExpression(
        Program program,
        String name,
        String type,
        List<Expression> indices,
        Expression expression
) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> value = expression.evaluate();

        if (!indices.isEmpty()) {
            List<Integer> newIndices = new ArrayList<>();

            for (Expression indexExpression : indices) {
                Value<?> indexValue = indexExpression.evaluate();

                if (indexValue instanceof IntegerValue index) {
                    newIndices.add(index.value());
                } else {
                    return null;
                }
            }

            Value<?> prevValue = program.getScope().get(name);

            if (prevValue instanceof ListValue listValue) {
                return program.getScope().set(name, ListValue.setNested(listValue, newIndices, value));
            }

            return null;
        }

        if (type.equals("=")) {
            return program.getScope().set(name, value);
        }

        Value<?> prev = program.getScope().get(name);
        return program.getScope().set(name, new ArithmeticOperator(type.equals("+=") ? "+" : "-", prev, value).evaluate());
    }

    public static Expression parse(Program program, String name) throws Exception {
        List<Expression> indices = new ArrayList<>();

        while (program.peekIs(Type.SQUARE_BRACE, "[")) {
            program.next();
            indices.add(Expression.parse(program));
            program.expect(Type.SQUARE_BRACE, "]");
        }

        String type = program.expect(Type.ASSIGN);
        Expression expression = Expression.parse(program);
        return new AssignmentExpression(program, name, type, indices, expression);
    }
}
