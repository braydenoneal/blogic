package com.braydenoneal.blang.parser.expression.builtin;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.Range;
import com.braydenoneal.blang.parser.expression.value.RangeValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record RangeBuiltin(Arguments arguments) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        int start = arguments.integerValue(program, "start", 0).value();
        int end = arguments.arguments().size() > 1 ? arguments.integerValue(program, "end", 1).value() : 0;
        int step = arguments.arguments().size() > 2 ? arguments.integerValue(program, "step", 2).value() : 1;

        return new RangeValue(new Range(start, end, step));
    }

    public static final MapCodec<RangeBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Arguments.CODEC.fieldOf("arguments").forGetter(RangeBuiltin::arguments)
    ).apply(instance, RangeBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.RANGE_BUILTIN;
    }
}
