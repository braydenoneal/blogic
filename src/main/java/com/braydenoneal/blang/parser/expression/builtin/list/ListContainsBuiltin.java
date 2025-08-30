package com.braydenoneal.blang.parser.expression.builtin.list;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.BooleanValue;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ListContainsBuiltin(
        ListValue listValue,
        Arguments arguments
) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        return new BooleanValue(listValue.value().contains(arguments.anyValue(program, "value").evaluate(program)));
    }

    public static final MapCodec<ListContainsBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ListValue.CODEC.fieldOf("listValue").forGetter(ListContainsBuiltin::listValue),
            Arguments.CODEC.fieldOf("arguments").forGetter(ListContainsBuiltin::arguments)
    ).apply(instance, ListContainsBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.LIST_CONTAINS_BUILTIN;
    }
}
