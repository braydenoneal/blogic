package com.braydenoneal.blang.parser.expression.operator;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.FloatValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ComparisonOperator(
        String operator,
        Expression operand_a,
        Expression operand_b
) implements Operator, Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> a = operand_a.evaluate(program);
        Value<?> b = operand_b.evaluate(program);

        if (operator.equals("==")) {
            return new BooleanValue(a.equals(b));
        } else if (operator.equals("!=")) {
            return new BooleanValue(!a.equals(b));
        }

        if (a instanceof IntegerValue a1 && b instanceof IntegerValue b1) {
            return switch (operator) {
                case "<=" -> new BooleanValue(a1.value() <= b1.value());
                case ">=" -> new BooleanValue(a1.value() >= b1.value());
                case "<" -> new BooleanValue(a1.value() < b1.value());
                default /* > */ -> new BooleanValue(a1.value() > b1.value());
            };
        } else if (a instanceof FloatValue a1 && b instanceof FloatValue b1) {
            return switch (operator) {
                case "<=" -> new BooleanValue(a1.value() <= b1.value());
                case ">=" -> new BooleanValue(a1.value() >= b1.value());
                case "<" -> new BooleanValue(a1.value() < b1.value());
                default /* > */ -> new BooleanValue(a1.value() > b1.value());
            };
        }

        throw new RunException("Operands are not comparable");
    }

    public static final MapCodec<ComparisonOperator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("operator").forGetter(ComparisonOperator::operator),
            Expression.CODEC.fieldOf("operand_a").forGetter(ComparisonOperator::operand_a),
            Expression.CODEC.fieldOf("operand_b").forGetter(ComparisonOperator::operand_b)
    ).apply(instance, ComparisonOperator::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.COMPARISON_OPERATOR;
    }
}
