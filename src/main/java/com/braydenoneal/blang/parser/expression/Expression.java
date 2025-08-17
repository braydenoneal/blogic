package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.expression.value.Value;

public interface Expression {
    Value<?> evaluate();
}
