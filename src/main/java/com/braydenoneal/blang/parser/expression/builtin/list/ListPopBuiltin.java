package com.braydenoneal.blang.parser.expression.builtin.list;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record ListPopBuiltin(
        String name,
        ListValue listValue
) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        List<Value<?>> localList = listValue.value();
        localList.removeLast();

        return program.getScope().set(name, new ListValue(localList));
    }

    public static final MapCodec<ListPopBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(ListPopBuiltin::name),
            ListValue.CODEC.fieldOf("listValue").forGetter(ListPopBuiltin::listValue)
    ).apply(instance, ListPopBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.LIST_POP_BUILTIN;
    }
}
