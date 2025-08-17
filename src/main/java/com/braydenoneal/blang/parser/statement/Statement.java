package com.braydenoneal.blang.parser.statement;

import com.braydenoneal.blang.parser.expression.value.Value;

public interface Statement {
    Value<?> execute();
}
