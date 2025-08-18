package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.operator.ArithmeticOperator;
import com.braydenoneal.blang.parser.expression.operator.BooleanOperator;
import com.braydenoneal.blang.parser.expression.operator.ComparisonOperator;
import com.braydenoneal.blang.parser.expression.value.*;
import com.braydenoneal.blang.tokenizer.Token;
import com.braydenoneal.blang.tokenizer.Type;

public interface Expression {
    Value<?> evaluate();

    static Expression parse(Program program) throws Exception {
        Token token = program.next();

        Expression expression = switch (token.type()) {
            case Type.BOOLEAN -> new BooleanValue(token.value().equals("true"));
            case Type.QUOTE -> new StringValue(token.value());
            case Type.FLOAT -> new FloatValue(Float.valueOf(token.value()));
            case Type.INTEGER -> new IntegerValue(Integer.valueOf(token.value()));
            default /* IDENTIFIER */ -> {
                if (program.peekIs(Type.PARENTHESIS, "(")) {
                    yield CallExpression.parse(program, token.value());
                } else {
                    yield new VariableExpression(program, token.value());
                }
            }
        };

        Type nextType = program.peek().type();
        Token operator;

        return switch (nextType) {
            case Type.BOOLEAN_OPERATOR -> {
                operator = program.next();
                yield new BooleanOperator(operator.value(), expression, parse(program));
            }
            case Type.COMPARISON_OPERATOR -> {
                operator = program.next();
                yield new ComparisonOperator(operator.value(), expression, parse(program));
            }
            case Type.ARITHMETIC_OPERATOR -> {
                operator = program.next();
                yield new ArithmeticOperator(operator.value(), expression, parse(program));
            }
            default -> expression;
        };
    }
}
