package com.braydenoneal.blang.parser.expression.operator;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.stream.Stream;

public record ArithmeticOperator(
        String operator,
        Expression operand_a,
        Expression operand_b
) implements Operator, Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> a = operand_a.evaluate(program);
        Value<?> b = operand_b.evaluate(program);

        if (a instanceof IntegerValue a1 && b instanceof FloatValue) {
            a = new FloatValue((float) a1.value());
        } else if (a instanceof FloatValue && b instanceof IntegerValue b1) {
            b = new FloatValue((float) b1.value());
        } else if (b instanceof StringValue) {
            a = new StringValue(a.value().toString());
        } else if (a instanceof StringValue) {
            b = new StringValue(b.value().toString());
        }

        if (a instanceof IntegerValue a1 && b instanceof IntegerValue b1) {
            return switch (operator) {
                case "+" -> new IntegerValue(a1.value() + b1.value());
                case "-" -> new IntegerValue(a1.value() - b1.value());
                case "*" -> new IntegerValue(a1.value() * b1.value());
                case "/" -> new IntegerValue(a1.value() / b1.value());
                case "%" -> new IntegerValue((a1.value() + b1.value()) % b1.value());
                default /* ^ */ -> new IntegerValue((int) Math.pow(a1.value(), b1.value()));
            };
        } else if (a instanceof FloatValue a1 && b instanceof FloatValue b1) {
            return switch (operator) {
                case "+" -> new FloatValue(a1.value() + b1.value());
                case "-" -> new FloatValue(a1.value() - b1.value());
                case "*" -> new FloatValue(a1.value() * b1.value());
                case "/" -> new FloatValue(a1.value() / b1.value());
                case "%" -> new FloatValue((a1.value() + b1.value()) % b1.value());
                default /* ^ */ -> new FloatValue((float) Math.pow(a1.value(), b1.value()));
            };
        } else if (operator.equals("+") && a instanceof StringValue a1 && b instanceof StringValue b1) {
            return new StringValue(a1.value() + b1.value());
        } else if (operator.equals("+") && a instanceof ListValue a1 && b instanceof ListValue b1) {
            return new ListValue(Stream.concat(a1.value().stream(), b1.value().stream()).toList());
        }

        throw new RunException("Invalid operands");
    }

    public static final MapCodec<ArithmeticOperator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("operator").forGetter(ArithmeticOperator::operator),
            Expression.CODEC.fieldOf("operand_a").forGetter(ArithmeticOperator::operand_a),
            Expression.CODEC.fieldOf("operand_b").forGetter(ArithmeticOperator::operand_b)
    ).apply(instance, ArithmeticOperator::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.ARITHMETIC_OPERATOR;
    }
}
