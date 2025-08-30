package com.braydenoneal.blang.parser.expression.builtin.list;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Arguments;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.braydenoneal.blang.parser.expression.value.IntegerValue;
import com.braydenoneal.blang.parser.expression.value.ListValue;
import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record ListRemoveBuiltin(
        String name,
        ListValue listValue,
        Arguments arguments
) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> removeValue = arguments.anyValue(program, "value");
        List<Value<?>> localList = listValue.value();

        if (removeValue instanceof IntegerValue integerValue) {
            localList.remove((int) integerValue.value());
        } else {
            localList.remove(removeValue);
        }

        return program.getScope().set(name, new ListValue(localList));
    }

    public static final MapCodec<ListRemoveBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(ListRemoveBuiltin::name),
            ListValue.CODEC.fieldOf("listValue").forGetter(ListRemoveBuiltin::listValue),
            Arguments.CODEC.fieldOf("arguments").forGetter(ListRemoveBuiltin::arguments)
    ).apply(instance, ListRemoveBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.LIST_REMOVE_BUILTIN;
    }
}
