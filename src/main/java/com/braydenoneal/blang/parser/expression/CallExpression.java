package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.parser.statement.FunctionDeclaration;

import java.util.ArrayList;
import java.util.List;

public record CallExpression(Program program, String name, List<Expression> arguments) implements Expression {
    @Override
    public Value<?> evaluate() {
        FunctionDeclaration function = program.getFunction(name);

        if (function != null) {
            program.newScope();
            List<String> names = new ArrayList<>(function.arguments().keySet());

            for (int i = 0; i < arguments.size(); i++) {
                program.getScope().set(names.get(i), arguments.get(i).evaluate());
            }

            Value<?> value = function.execute();
            program.endScope();
            return value;
        }

        return null;
    }
}
