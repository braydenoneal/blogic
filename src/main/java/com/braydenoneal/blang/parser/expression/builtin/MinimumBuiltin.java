package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.FloatValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.StringValue;
import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public record MinimumBuiltin(List<Expression> arguments) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> a = arguments.getFirst().evaluate();
        Value<?> b = arguments.get(1).evaluate();

        if (a instanceof IntegerValue a1 && b instanceof FloatValue) {
            a = new FloatValue((float) a1.value());
        } else if (a instanceof FloatValue && b instanceof IntegerValue b1) {
            b = new FloatValue((float) b1.value());
        } else if (b instanceof StringValue) {
            a = new StringValue(a.value().toString());
        } else if (a instanceof StringValue) {
            b = new StringValue(b.value().toString());
        }

        if (a instanceof IntegerValue a1 && b instanceof IntegerValue b1) {
            return new IntegerValue(Math.min(a1.value(), b1.value()));
        } else if (a instanceof FloatValue a1 && b instanceof FloatValue b1) {
            return new FloatValue(Math.min(a1.value(), b1.value()));
        }

        return null;
    }
}
