package com.braydenoneal.blang.parser.expression.operator;

import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.Value;

public record TernaryOperator(
        Expression operand_a,
        Expression operand_b,
        Expression operand_c
) implements Operator, Expression {
    @Override
    public Value<?> evaluate() {
        return ((BooleanValue) operand_a.evaluate()).value() ? operand_b.evaluate() : operand_c.evaluate();
    }
}
