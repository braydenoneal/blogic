package com.braydenoneal.blang.parser.expression.operator;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.Value;

public record BooleanOperator(
        String operator,
        Expression operand_a,
        Expression operand_b
) implements Operator, Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> a = operand_a.evaluate(program);
        Value<?> b = operand_b.evaluate(program);

        if (a instanceof BooleanValue a1 && b instanceof BooleanValue b1) {
            if (operator.equals("and")) {
                return new BooleanValue(a1.value() && b1.value());
            } else {
                return new BooleanValue(a1.value() || b1.value());
            }
        }

        System.out.println("boolean operator");
        System.out.println(operator);
        System.out.println(operand_a);
        System.out.println(operand_b);
        return null;
    }
}
