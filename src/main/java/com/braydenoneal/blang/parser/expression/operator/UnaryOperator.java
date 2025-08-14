package com.braydenoneal.blang.parser.expression.operator;

import com.braydenoneal.blang.parser.expression.Expression;

public record UnaryOperator(String operator, Expression operand) implements Operator {
}
