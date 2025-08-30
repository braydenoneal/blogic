package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record ListAccessExpression(Expression listExpression, List<Expression> indices) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> listValue = listExpression.evaluate(program);

        if (listValue instanceof ListValue list) {
            return ListValue.getNested(list, ListValue.toIndexValues(program, indices));
        }

        throw new RunException("Expression is not a list");
    }

    public static final MapCodec<ListAccessExpression> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Expression.CODEC.fieldOf("listExpression").forGetter(ListAccessExpression::listExpression),
            Codec.list(Expression.CODEC).fieldOf("indices").forGetter(ListAccessExpression::indices)
    ).apply(instance, ListAccessExpression::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.LIST_ACCESS_EXPRESSION;
    }
}
