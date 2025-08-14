package com.braydenoneal.blang.parser.expression.operator;

import com.braydenoneal.blang.parser.expression.Expression;

public record BinaryOperator(String operator, Expression operand_a, Expression operand_b) implements Operator {
}
