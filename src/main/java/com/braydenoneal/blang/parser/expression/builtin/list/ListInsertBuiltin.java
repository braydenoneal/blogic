package com.braydenoneal.blang.parser.expression.builtin.list;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public record ListInsertBuiltin(
        Program program,
        String name,
        ListValue listValue,
        List<Expression> arguments
) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> indexValue = arguments.getFirst().evaluate();
        Value<?> insertValue = arguments.get(1).evaluate();

        if (indexValue instanceof IntegerValue integerValue) {
            List<Value<?>> localList = listValue.value();
            localList.add(integerValue.value(), insertValue);
            return program.getScope().set(name, new ListValue(localList));
        }

        System.out.println("listInsert");
        System.out.println(indexValue);
        System.out.println(insertValue);
        return null;
    }
}
