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

public record ListInsertBuiltin(
        String name,
        ListValue listValue,
        Arguments arguments
) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        int index = arguments.integerValue(program, "index").value();
        Value<?> insertValue = arguments.anyValue(program, "value");

        List<Value<?>> localList = listValue.value();
        localList.add(index, insertValue);
        return program.getScope().set(name, new ListValue(localList));
    }

    public static final MapCodec<ListInsertBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(ListInsertBuiltin::name),
            ListValue.CODEC.fieldOf("listValue").forGetter(ListInsertBuiltin::listValue),
            Arguments.CODEC.fieldOf("arguments").forGetter(ListInsertBuiltin::arguments)
    ).apply(instance, ListInsertBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.LIST_INSERT_BUILTIN;
    }
}
