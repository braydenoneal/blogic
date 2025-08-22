package com.braydenoneal.blang.parser.expression.builtin.list;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;

import java.util.List;

public record ListSetBuiltin(
        Program program,
        String name,
        ListValue listValue,
        List<Expression> arguments
) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> indexValue = arguments.getFirst().evaluate();
        Value<?> setValue = arguments.get(1).evaluate();

        if (indexValue instanceof IntegerValue index) {
            List<Value<?>> localList = listValue.value();
            localList.set(index.value(), setValue);
            program.getScope().set(name, new ListValue(localList));
            return null;
        }

        System.out.println("listSet");
        System.out.println(indexValue);
        System.out.println(setValue);
        return null;
    }
}
