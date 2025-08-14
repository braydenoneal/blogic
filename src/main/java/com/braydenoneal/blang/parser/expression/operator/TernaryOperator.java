package com.braydenoneal.blang.parser.expression.operator;

import com.braydenoneal.blang.parser.expression.Expression;

public record TernaryOperator(
        String operator,
        Expression operand_a,
        Expression operand_b,
        Expression operand_c
) implements Operator {
}
