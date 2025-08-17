package com.braydenoneal.blang.parser.expression.value;

import com.braydenoneal.blang.parser.expression.Expression;

public class Value<T> implements Expression {
    private final T value;

    public Value(T value) {
        this.value = value;
    }

    public T value() {
        return value;
    }

    @Override
    public Value<?> evaluate() {
        return this;
    }
}
