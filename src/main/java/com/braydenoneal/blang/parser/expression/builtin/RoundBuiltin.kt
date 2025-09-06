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

public record RoundBuiltin(Arguments arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> value = arguments.anyValue(program, "value", 0);

        if (value instanceof IntegerValue) {
            return value;
        } else if (value instanceof FloatValue floatValue) {
            return new IntegerValue(Math.round(floatValue.value()));
        }

        throw new RunException("Expression is not a number");
    }

    public static final MapCodec<RoundBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Arguments.CODEC.fieldOf("arguments").forGetter(RoundBuiltin::arguments)
    ).apply(instance, RoundBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.ROUND_BUILTIN;
    }
}
