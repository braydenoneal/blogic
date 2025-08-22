package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public record NamedListAccessExpression(
        String name,
        Expression listExpression,
        List<Expression> indices
) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> listValue = listExpression.evaluate();

        if (listValue instanceof ListValue list) {
            return ListValue.getNested(list, ListValue.toIndexValues(indices));
        }

        System.out.println("listAccess");
        System.out.println(listValue);
        System.out.println(indices);
        return null;
    }
}
