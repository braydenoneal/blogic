package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.ParseException;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.builtin.BuiltinExpression;
import com.braydenoneal.blang.parser.expression.builtin.list.*;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.parser.statement.ImportStatement;
import com.braydenoneal.blang.tokenizer.Type;
import com.braydenoneal.block.entity.ControllerBlockEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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

        throw new RunException("Member is not a variable nor a list");
    }

    public static Expression parse(Program program, Expression member) throws ParseException {
        program.expect(Type.DOT);
        String functionName = program.expect(Type.IDENTIFIER);
        List<Expression> arguments = BuiltinExpression.parseArguments(program);
        return new MemberCallExpression(member, functionName, arguments);
    }

    public static final MapCodec<MemberCallExpression> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Expression.CODEC.fieldOf("member").forGetter(MemberCallExpression::member),
            Codec.STRING.fieldOf("functionName").forGetter(MemberCallExpression::functionName),
            Codec.list(Expression.CODEC).fieldOf("arguments").forGetter(MemberCallExpression::arguments)
    ).apply(instance, MemberCallExpression::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.MEMBER_CALL_EXPRESSION;
    }
}
