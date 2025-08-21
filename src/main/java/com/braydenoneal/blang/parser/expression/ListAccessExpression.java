package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;

public record ListAccessExpression(Expression listExpression, Expression indexExpression) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> listValue = listExpression.evaluate();
        Value<?> indexValue = indexExpression.evaluate();

        if (listValue instanceof ListValue list && indexValue instanceof IntegerValue index) {
            if (index.value() < 0) {
                return list.value().get(list.value().size() - index.value());
            }

            return list.value().get(index.value());
        }

        System.out.println("listAccess");
        System.out.println(listValue);
        System.out.println(indexValue);
        return null;
    }
}
