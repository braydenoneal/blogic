package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.FloatValue;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record MinimumBuiltin(Arguments arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> a = arguments.anyValue(program, "a");
        Value<?> b = arguments.anyValue(program, "b");

        if (a instanceof IntegerValue a1 && b instanceof FloatValue) {
            a = new FloatValue((float) a1.value());
        } else if (a instanceof FloatValue && b instanceof IntegerValue b1) {
            b = new FloatValue((float) b1.value());
        }

        if (a instanceof IntegerValue a1 && b instanceof IntegerValue b1) {
            return new IntegerValue(Math.min(a1.value(), b1.value()));
        } else if (a instanceof FloatValue a1 && b instanceof FloatValue b1) {
            return new FloatValue(Math.min(a1.value(), b1.value()));
        }

        throw new RunException("Arguments are not numbers");
    }

    public static final MapCodec<MinimumBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Arguments.CODEC.fieldOf("arguments").forGetter(MinimumBuiltin::arguments)
    ).apply(instance, MinimumBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.MINIMUM_BUILTIN;
    }
}
