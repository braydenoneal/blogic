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

public record AbsoluteValueBuiltin(Arguments arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> value = arguments().anyValue(program, "value", 0);

        if (value instanceof IntegerValue integerValue) {
            return new IntegerValue(Math.abs(integerValue.value()));
        } else if (value instanceof FloatValue floatValue) {
            return new FloatValue(Math.abs(floatValue.value()));
        }

        throw new RunException("Expression is not a number");
    }

    public static final MapCodec<AbsoluteValueBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Arguments.CODEC.fieldOf("arguments").forGetter(AbsoluteValueBuiltin::arguments)
    ).apply(instance, AbsoluteValueBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.ABSOLUTE_VALUE_BUILTIN;
    }
}
