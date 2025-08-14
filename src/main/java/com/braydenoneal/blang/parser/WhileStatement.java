package com.braydenoneal.blang.parser;

import com.braydenoneal.blang.parser.expression.Expression;

import java.util.List;

public record WhileStatement(
        Expression condition,
        List<Statement> statements
) implements Statement {
}
