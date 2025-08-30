package com.braydenoneal.blang.parser.expression.value;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public class ListValue extends Value<List<Value<?>>> {
    public static final MapCodec<ListValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.list(Value.CODEC).fieldOf("value").forGetter(ListValue::value)
    ).apply(instance, ListValue::new));

    public ListValue(List<Value<?>> value) {
        super(value);
    }

    @Override
    public ValueType<?> getValueType() {
        return ValueTypes.LIST;
    }

    @Override
    public String toString() {
        StringBuilder print = new StringBuilder("[");

        for (int i = 0; i < value().size(); i++) {
            print.append(value().get(i).toString());

            if (i < value().size() - 1) {
                print.append(", ");
            }
        }

        return print + "]";
    }

    public static List<Value<?>> toIndexValues(Program program, List<Expression> expressions) {
        List<Value<?>> indices = new ArrayList<>();

        for (Expression expression : expressions) {
            indices.add(expression.evaluate(program));
        }

        return indices;
    }

    public static ListValue setNested(ListValue list, List<? extends Value<?>> indexValues, Value<?> value) {
        List<Value<?>> newList = new ArrayList<>(list.value());
        Value<?> indexValue = indexValues.getFirst();

        for (int i = 0; i < newList.size(); i++) {
            if (indexValue instanceof IntegerValue index && index.value() != i) {
                continue;
            }

            if (indexValues.size() > 1 && newList.get(i) instanceof ListValue nestedList) {
                newList.set(i, setNested(nestedList, indexValues.subList(1, indexValues.size()), value));
            } else {
                newList.set(i, value);
            }

            break;
        }

        return new ListValue(newList);
    }

    public static Value<?> getNested(ListValue list, List<? extends Value<?>> indexValues) {
        Value<?> indexValue = indexValues.getFirst();

        for (int i = 0; i < list.value().size(); i++) {
            if (indexValue instanceof IntegerValue index && index.value() != i) {
                continue;
            }

            if (indexValues.size() > 1 && list.value().get(i) instanceof ListValue nestedList) {
                return getNested(nestedList, indexValues.subList(1, indexValues.size()));
            } else {
                return list.value().get(i);
            }
        }

        System.out.println("getNested");
        System.out.println(list);
        return null;
    }
}
