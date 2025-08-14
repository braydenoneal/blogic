package com.braydenoneal.blang.parser.expression;

import java.util.List;

public record StructCreation(Expression struct, List<Expression> values) {
}
