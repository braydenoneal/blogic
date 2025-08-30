package com.braydenoneal.blang.parser.expression.builtin.list;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public record ListPopBuiltin(
        String name,
        ListValue listValue
) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        List<Value<?>> localList = listValue.value();
        localList.removeLast();

        return program.getScope().set(name, new ListValue(localList));
    }
}
