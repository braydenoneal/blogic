package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.builtin.BuiltinExpression;
import com.braydenoneal.blang.parser.expression.builtin.list.*;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.parser.statement.ImportStatement;
import com.braydenoneal.blang.tokenizer.Type;
import com.braydenoneal.block.entity.ControllerBlockEntity;

import java.util.List;

public record MemberCallExpression(
        Expression member,
        String functionName,
        List<Expression> arguments
) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        if (member instanceof VariableExpression(String name)) {
            for (ImportStatement importStatement : program.imports()) {
                if (importStatement.identifiers().getLast().equals(name)) {
                    Program importProgram = program;

                    for (String importName : importStatement.identifiers()) {
                        for (ControllerBlockEntity controller : importProgram.context().entity().getConnectedControllerBlockEntities()) {
                            if (controller.program().name().equals(importName)) {
                                importProgram = controller.program();
                            }
                        }
                    }

                    CallExpression callExpression = new CallExpression(functionName, arguments);
                    return callExpression.call(program, importProgram);
                }
            }

            Value<?> object = program.getScope().get(name);

            if (object instanceof ListValue listValue) {
                switch (functionName) {
                    case "append":
                        return new ListAppendBuiltin(name, listValue, arguments).evaluate(program);
                    case "insert":
                        return new ListInsertBuiltin(name, listValue, arguments).evaluate(program);
                    case "remove":
                        return new ListRemoveBuiltin(name, listValue, arguments).evaluate(program);
                    case "pop":
                        return new ListPopBuiltin(name, listValue).evaluate(program);
                    case "contains":
                        return new ListContainsBuiltin(listValue, arguments).evaluate(program);
                    case "containsAll":
                        return new ListContainsAllBuiltin(listValue, arguments).evaluate(program);
                }
            }
        } else {
            Value<?> value = member.evaluate(program);

            if (value instanceof ListValue listValue) {
                switch (functionName) {
                    case "contains":
                        return new ListContainsBuiltin(listValue, arguments).evaluate(program);
                    case "containsAll":
                        return new ListContainsAllBuiltin(listValue, arguments).evaluate(program);
                }
            }
        }

        System.out.println("memberCall");
        System.out.println(member);
        System.out.println(functionName);
        System.out.println(arguments);
        return null;
    }

    public static Expression parse(Program program, Expression member) throws Exception {
        program.expect(Type.DOT);
        String functionName = program.expect(Type.IDENTIFIER);
        List<Expression> arguments = BuiltinExpression.parseArguments(program);
        return new MemberCallExpression(member, functionName, arguments);
    }
}
