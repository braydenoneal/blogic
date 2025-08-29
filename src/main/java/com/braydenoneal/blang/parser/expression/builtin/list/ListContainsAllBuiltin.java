package com.braydenoneal.blang.parser.expression.builtin.list;

import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public record ListContainsAllBuiltin(
        ListValue listValue,
        List<Expression> arguments
) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> nextListValue = arguments.getFirst().evaluate();

        if (nextListValue instanceof ListValue list) {
            return new BooleanValue(listValue.value().containsAll(list.value()));
        }

        System.out.println("containsAll");
        System.out.println(arguments.getFirst());
        return null;
    }
}
