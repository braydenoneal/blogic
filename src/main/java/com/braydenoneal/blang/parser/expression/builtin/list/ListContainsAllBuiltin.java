package com.braydenoneal.blang.parser.expression.builtin.list;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ListContainsAllBuiltin(
        ListValue listValue,
        Arguments arguments
) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> nextListValue = arguments.anyValue(program, "value");

        if (nextListValue instanceof ListValue list) {
            return new BooleanValue(listValue.value().containsAll(list.value()));
        }

        throw new RunException("Expression is not a list");
    }

    public static final MapCodec<ListContainsAllBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ListValue.CODEC.fieldOf("listValue").forGetter(ListContainsAllBuiltin::listValue),
            Arguments.CODEC.fieldOf("arguments").forGetter(ListContainsAllBuiltin::arguments)
    ).apply(instance, ListContainsAllBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.LIST_CONTAINS_ALL_BUILTIN;
    }
}
