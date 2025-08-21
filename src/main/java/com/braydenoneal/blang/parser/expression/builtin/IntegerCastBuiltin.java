package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.FloatValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.Value;

public record IntegerCastBuiltin(Expression expression) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> value = expression.evaluate();

        if (value instanceof FloatValue floatValue) {
            return new IntegerValue(floatValue.value().intValue());
        }

        System.out.println("int");
        System.out.println(value);
        return null;
    }
}
