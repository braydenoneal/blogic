package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.ParseException;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.operator.ArithmeticOperator;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record AssignmentExpression(
        String type,
        Expression variableExpression,
        Expression expression
) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> value = expression.evaluate(program);

        if (variableExpression instanceof VariableExpression(String name)) {
            if (type.equals("=")) {
                return program.getScope().set(name, value);
            }

            Value<?> prev = program.getScope().get(name);
            return program.getScope().set(name, new ArithmeticOperator(type.equals("+=") ? "+" : "-", prev, value).evaluate(program));
        } else if (variableExpression instanceof NamedListAccessExpression(
                String name, Expression listExpression, List<Expression> indices
        )) {
            Value<?> listValue = listExpression.evaluate(program);

            if (listValue instanceof ListValue list) {
                List<Value<?>> indexValues = ListValue.toIndexValues(program, indices);

                if (type.equals("=")) {
                    return program.getScope().set(name, ListValue.setNested(list, indexValues, value));
                }

                Value<?> prev = ListValue.getNested(list, indexValues);
                return program.getScope().set(name, ListValue.setNested(list, indexValues, new ArithmeticOperator(type.equals("+=") ? "+" : "-", prev, value).evaluate(program)));
            }
        }

        throw new RunException("Expression is not a variable nor named list access");
    }

    public static Expression parse(Program program, Expression variableExpression) throws ParseException {
        String type = program.expect(Type.ASSIGN);
        Expression expression = Expression.parse(program);
        return new AssignmentExpression(type, variableExpression, expression);
    }

    public static final MapCodec<AssignmentExpression> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("type").forGetter(AssignmentExpression::type),
            Expression.CODEC.fieldOf("variable_expression").forGetter(AssignmentExpression::variableExpression),
            Expression.CODEC.fieldOf("expression").forGetter(AssignmentExpression::variableExpression)
    ).apply(instance, AssignmentExpression::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.ASSIGNMENT_EXPRESSION;
    }
}
