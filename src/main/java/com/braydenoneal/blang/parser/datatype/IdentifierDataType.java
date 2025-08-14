package com.braydenoneal.blang.parser.datatype;

import com.braydenoneal.blang.parser.expression.Expression;

public record IdentifierDataType(Expression expression, int listLevel) implements DataType {
}
