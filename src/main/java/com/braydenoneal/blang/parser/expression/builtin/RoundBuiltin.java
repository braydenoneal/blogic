package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.FloatValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.Value;

public record RoundBuiltin(Expression expression) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> value = expression.evaluate(program);

        if (value instanceof IntegerValue) {
            return value;
        } else if (value instanceof FloatValue floatValue) {
            return new IntegerValue(Math.round(floatValue.value()));
        }

        System.out.println("round");
        System.out.println(value);
        return null;
    }
}
