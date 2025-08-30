package com.braydenoneal.blang.parser.expression.operator;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.Value;

public record UnaryOperator(Expression operand) implements Operator, Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> value = operand.evaluate(program);

        if (value instanceof BooleanValue value1) {
            return new BooleanValue(!value1.value());
        }

        System.out.println(operand);
        return null;
    }
}
