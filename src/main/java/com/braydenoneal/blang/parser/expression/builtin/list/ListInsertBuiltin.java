package com.braydenoneal.blang.parser.expression.builtin.list;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.RunException;
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

public record ListInsertBuiltin(
        String name,
        ListValue listValue,
        List<Expression> arguments
) implements Expression {
    @Override
    public Value<?> evaluate(Program program) {
        Value<?> indexValue = arguments.getFirst().evaluate(program);
        Value<?> insertValue = arguments.get(1).evaluate(program);

        if (indexValue instanceof IntegerValue integerValue) {
            List<Value<?>> localList = listValue.value();
            localList.add(integerValue.value(), insertValue);
            return program.getScope().set(name, new ListValue(localList));
        }

        throw new RunException("Expression is not an integer");
    }

    public static final MapCodec<ListInsertBuiltin> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(ListInsertBuiltin::name),
            ListValue.CODEC.fieldOf("listValue").forGetter(ListInsertBuiltin::listValue),
            Codec.list(Expression.CODEC).fieldOf("arguments").forGetter(ListInsertBuiltin::arguments)
    ).apply(instance, ListInsertBuiltin::new));

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.LIST_INSERT_BUILTIN;
    }
}
