package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.builtin.BuiltinExpression;
import com.braydenoneal.blang.parser.expression.builtin.list.ListAppendBuiltin;
import com.braydenoneal.blang.parser.expression.builtin.list.ListSetBuiltin;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;

import java.util.List;

public record MemberCallExpression(
        Program program,
        String name,
        String functionName,
        List<Expression> arguments
) implements Expression {
    @Override
    public Value<?> evaluate() {
        Value<?> object = program.getScope().get(name);

        if (object instanceof ListValue listValue) {
            switch (functionName) {
                case "set":
                    return new ListSetBuiltin(program, name, listValue, arguments).evaluate();
                case "append":
                    return new ListAppendBuiltin(program, name, listValue, arguments).evaluate();
            }
        }

        System.out.println("memberCall");
        System.out.println(object);
        return null;
    }

    public static Expression parse(Program program, String name) throws Exception {
        program.expect(Type.DOT);
        String functionName = program.expect(Type.IDENTIFIER);
        List<Expression> arguments = BuiltinExpression.parseArguments(program);
        return new MemberCallExpression(program, name, functionName, arguments);
    }
}
