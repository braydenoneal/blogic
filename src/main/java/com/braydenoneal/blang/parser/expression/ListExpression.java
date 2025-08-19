package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.braydenoneal.blang.tokenizer.Type;

import java.util.ArrayList;
import java.util.List;

public record ListExpression(Program program, List<Expression> expressions) implements Expression {
    @Override
    public Value<?> evaluate() {
        List<Value<?>> values = new ArrayList<>();

        for (Expression expression : expressions) {
            values.add(expression.evaluate());
        }

        return new ListValue(values);
    }

    public static Expression parse(Program program) throws Exception {
        List<Expression> expressions = new ArrayList<>();
        program.expect(Type.SQUARE_BRACE, "[");

        while (!program.peekIs(Type.SQUARE_BRACE, "]")) {
            expressions.add(Expression.parse(program));

            if (!program.peekIs(Type.SQUARE_BRACE, "]")) {
                program.expect(Type.COMMA);
            }
        }

        program.expect(Type.SQUARE_BRACE, "]");
        return new ListExpression(program, expressions);
    }
}
