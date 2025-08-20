package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.StringValue;
import com.braydenoneal.blang.parser.expression.value.Value;

public record PrintBuiltin(Expression expression) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> value = expression.evaluate();
        String string = value.toString();

        if (value instanceof StringValue) {
            string = string.substring(1, string.length() - 1);
        }

        System.out.println(string);
        return null;
    }
}
