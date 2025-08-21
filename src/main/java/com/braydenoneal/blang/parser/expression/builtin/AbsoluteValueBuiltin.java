package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.FloatValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.Value;

public record AbsoluteValueBuiltin(Expression expression) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> value = expression.evaluate();

        if (value instanceof IntegerValue integerValue) {
            return new IntegerValue(Math.abs(integerValue.value()));
        } else if (value instanceof FloatValue floatValue) {
            return new FloatValue(Math.abs(floatValue.value()));
        }

        System.out.println("abs");
        System.out.println(value);
        return null;
    }
}
