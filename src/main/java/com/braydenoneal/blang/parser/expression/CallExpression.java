package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.parser.statement.FunctionDeclaration;

import java.util.List;

public record CallExpression(Program program, String name, List<Expression> arguments) implements Expression {
    @Override
    public Value<?> evaluate() {
        FunctionDeclaration function = program.getFunction(name);

        if (function != null) {
            program.newScope();

            for (int i = 0; i < arguments.size(); i++) {
                program.getScope().set(function.arguments().get(i), arguments.get(i).evaluate());
            }

            Value<?> value = function.execute();
            program.endScope();
            return value;
        }

        return null;
    }

    public static Expression parse(Program program) {
        return null;
    }
}
