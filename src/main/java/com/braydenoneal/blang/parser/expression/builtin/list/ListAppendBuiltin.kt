package com.braydenoneal.blang.parser.expression.builtin.list;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record ListAppendBuiltin(
        String name,
        ListValue listValue,
        Arguments arguments
) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> appendValue = arguments.anyValue(program, "value", 0);

        List<Value<?>> localList = listValue.value();
        localList.add(appendValue);

        return program.getScope().set(name, new ListValue(localList));
    }

    public static final MapCodec<ListAppendBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(ListAppendBuiltin::name),
            ListValue.CODEC.fieldOf("listValue").forGetter(ListAppendBuiltin::listValue),
            Arguments.CODEC.fieldOf("arguments").forGetter(ListAppendBuiltin::arguments)
    ).apply(instance, ListAppendBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.LIST_APPEND_BUILTIN;
    }
}
