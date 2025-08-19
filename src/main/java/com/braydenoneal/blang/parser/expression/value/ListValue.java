package com.braydenoneal.blang.parser.expression.value;

import java.util.List;

public class ListValue extends Value<List<Value<?>>> {
    public ListValue(List<Value<?>> value) {
        super(value);
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
}
