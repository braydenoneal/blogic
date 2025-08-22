package com.braydenoneal.blang.parser.expression.value;

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
    public ValueType<?> getType() {
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

    public static ListValue setNested(ListValue list, List<Integer> indices, Value<?> value) {
        List<Value<?>> newList = new ArrayList<>(list.value());

        for (int i = 0; i < newList.size(); i++) {
            if (indices.getFirst() != i) {
                break;
            }

            if (indices.size() > 1 && newList.get(i) instanceof ListValue nestedList) {
                newList.set(i, setNested(nestedList, indices.subList(1, newList.size() + 1), value));
            } else {
                newList.set(i, value);
            }
        }

        return new ListValue(newList);
    }
}
