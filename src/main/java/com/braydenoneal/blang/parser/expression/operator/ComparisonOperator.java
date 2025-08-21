package com.braydenoneal.blang.parser.expression.operator;

import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.FloatValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.Value;

public record ComparisonOperator(
        String operator,
        Expression operand_a,
        Expression operand_b
) implements Operator, Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> a = operand_a.evaluate();
        Value<?> b = operand_b.evaluate();

        if (operator.equals("==")) {
            return new BooleanValue(a.value() == b.value());
        } else if (operator.equals("!=")) {
            return new BooleanValue(a.value() != b.value());
        }

        if (a instanceof IntegerValue a1 && b instanceof IntegerValue b1) {
            return switch (operator) {
                case "<=" -> new BooleanValue(a1.value() <= b1.value());
                case ">=" -> new BooleanValue(a1.value() >= b1.value());
                case "<" -> new BooleanValue(a1.value() < b1.value());
                default /* > */ -> new BooleanValue(a1.value() > b1.value());
            };
        } else if (a instanceof FloatValue a1 && b instanceof FloatValue b1) {
            return switch (operator) {
                case "<=" -> new BooleanValue(a1.value() <= b1.value());
                case ">=" -> new BooleanValue(a1.value() >= b1.value());
                case "<" -> new BooleanValue(a1.value() < b1.value());
                default /* > */ -> new BooleanValue(a1.value() > b1.value());
            };
        }

        System.out.println(operator);
        System.out.println(operand_a);
        System.out.println(operand_b);
        return null;
    }
}
