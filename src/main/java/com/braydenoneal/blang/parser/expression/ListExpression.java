package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.ParseException;
import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public record ListExpression(List<Expression> expressions) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        return new ListValue(ListValue.toIndexValues(program, expressions));
    }

    public static Expression parse(Program program) throws ParseException {
        List<Expression> expressions = new ArrayList<>();
        program.expect(Type.SQUARE_BRACE, "[");

        while (!program.peekIs(Type.SQUARE_BRACE, "]")) {
            expressions.add(Expression.parse(program));

            if (!program.peekIs(Type.SQUARE_BRACE, "]")) {
                program.expect(Type.COMMA);
            }
        }

        program.expect(Type.SQUARE_BRACE, "]");
        return new ListExpression(expressions);
    }

    public static final MapCodec<ListExpression> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.list(Expression.CODEC).fieldOf("expressions").forGetter(ListExpression::expressions)
    ).apply(instance, ListExpression::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.LIST_EXPRESSION;
    }
}
