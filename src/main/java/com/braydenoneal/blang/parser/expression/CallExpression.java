package com.braydenoneal.blang.parser.expression;

import java.util.List;

public record CallExpression(Expression callee, List<Expression> arguments) {
}
