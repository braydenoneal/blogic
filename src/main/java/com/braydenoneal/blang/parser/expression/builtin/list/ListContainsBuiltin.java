package com.braydenoneal.blang.parser.expression.builtin.list;

import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public record ListContainsBuiltin(
        ListValue listValue,
        List<Expression> arguments
) implements Expression {
    @Override
    public Value<?> evaluate() {
        return new BooleanValue(listValue.value().contains(arguments.getFirst().evaluate()));
    }
}
