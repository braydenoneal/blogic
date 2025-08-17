package com.braydenoneal.blang.parser;

import com.braydenoneal.blang.parser.expression.value.Value;

public interface Statement {
    Value<?> execute();
}
