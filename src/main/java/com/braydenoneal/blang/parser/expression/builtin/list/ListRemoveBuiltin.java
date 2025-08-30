package com.braydenoneal.blang.parser.expression.builtin.list;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public record ListRemoveBuiltin(
        String name,
        ListValue listValue,
        List<Expression> arguments
) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> removeValue = arguments.getFirst().evaluate(program);

        List<Value<?>> localList = listValue.value();

        if (removeValue instanceof IntegerValue integerValue) {
            localList.remove((int) integerValue.value());
        } else {
            localList.remove(removeValue);
        }

        return program.getScope().set(name, new ListValue(localList));
    }
}
