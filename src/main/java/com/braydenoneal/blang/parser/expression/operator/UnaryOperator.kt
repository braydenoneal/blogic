package com.braydenoneal.blang.parser.expression.operator;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record UnaryOperator(Expression operand) implements Operator, Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> value = operand.evaluate(program);

        if (value instanceof BooleanValue value1) {
            return new BooleanValue(!value1.value());
        }

        throw new RunException("Operand is not a boolean");
    }

    public static final MapCodec<UnaryOperator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Expression.CODEC.fieldOf("operand").forGetter(UnaryOperator::operand)
    ).apply(instance, UnaryOperator::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.UNARY_OPERATOR;
    }
}
