package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.FloatValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.StringValue;
import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public record MaximumBuiltin(List<Expression> arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> a = arguments.getFirst().evaluate(program);
        Value<?> b = arguments.get(1).evaluate(program);

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
            return new IntegerValue(Math.max(a1.value(), b1.value()));
        } else if (a instanceof FloatValue a1 && b instanceof FloatValue b1) {
            return new FloatValue(Math.max(a1.value(), b1.value()));
        }

        System.out.println("max");
        System.out.println(a);
        System.out.println(b);
        return null;
    }
}
