package com.braydenoneal.blang.parser.expression;

public record AssignmentExpression(String name, String operator, Expression expression) {
}
