package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.operator.ArithmeticOperator;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;

import java.util.List;

public record AssignmentExpression(
        String type,
        Expression variableExpression,
        Expression expression
) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> value = expression.evaluate(program);

        if (variableExpression instanceof VariableExpression(String name)) {

            if (type.equals("=")) {
                return program.getScope().set(name, value);
            }

            Value<?> prev = program.getScope().get(name);
            return program.getScope().set(name, new ArithmeticOperator(type.equals("+=") ? "+" : "-", prev, value).evaluate(program));
        } else if (variableExpression instanceof NamedListAccessExpression(
                String name, Expression listExpression, List<Expression> indices
        )) {
            Value<?> listValue = listExpression.evaluate(program);

            if (listValue instanceof ListValue list) {
                List<Value<?>> indexValues = ListValue.toIndexValues(program, indices);

                if (type.equals("=")) {
                    return program.getScope().set(name, ListValue.setNested(list, indexValues, value));
                }

                Value<?> prev = ListValue.getNested(list, indexValues);
                return program.getScope().set(name, ListValue.setNested(list, indexValues, new ArithmeticOperator(type.equals("+=") ? "+" : "-", prev, value).evaluate(program)));
            }
        }

        System.out.println("assignmentExpression");
        System.out.println(variableExpression);
        System.out.println(value);
        return null;
    }

    public static Expression parse(Program program, Expression variableExpression) throws Exception {
        String type = program.expect(Type.ASSIGN);
        Expression expression = Expression.parse(program);
        return new AssignmentExpression(type, variableExpression, expression);
    }
}
