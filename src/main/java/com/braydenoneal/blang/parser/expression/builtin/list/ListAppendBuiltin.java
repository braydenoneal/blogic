package com.braydenoneal.blang.parser.expression.builtin.list;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public record ListAppendBuiltin(
        Program program,
        String name,
        ListValue listValue,
        List<Expression> arguments
) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> appendValue = arguments.getFirst().evaluate();

        List<Value<?>> localList = listValue.value();
        localList.add(appendValue);
        program.getScope().set(name, new ListValue(localList));

        return null;
    }
}
