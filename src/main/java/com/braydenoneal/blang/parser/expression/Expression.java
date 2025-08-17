package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.value.*;
import com.braydenoneal.blang.tokenizer.Token;
import com.braydenoneal.blang.tokenizer.Type;

public interface Expression {
    Value<?> evaluate();

    static Expression parse(Program program) {
        Token token = program.next();

        Expression expression = switch (token.type()) {
            case Type.BOOLEAN -> new BooleanValue(token.value().equals("true"));
            case Type.QUOTE -> new StringValue(token.value());
            case Type.FLOAT -> new FloatValue(Float.valueOf(token.value()));
            case Type.INTEGER -> new IntegerValue(Integer.valueOf(token.value()));
            default /* IDENTIFIER */ -> {
                if (program.peekIs(Type.PARENTHESIS, "(")) {
                    yield CallExpression.parse(program);
                } else {
                    yield new VariableExpression(program, token.value());
                }
            }
        };

        return expression;
    }
}
