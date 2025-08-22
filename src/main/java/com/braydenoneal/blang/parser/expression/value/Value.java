package com.braydenoneal.blang.parser.expression.value;

import com.braydenoneal.blang.parser.expression.Expression;
import com.mojang.serialization.Codec;

public abstract class Value<T> implements Expression {
    private T value;

    public Value(T value) {
        this.value = value;
    }

    public T value() {
        return value;
    }

    public void setValue(T newValue) {
        value = newValue;
    }

    @Override
    public Value<?> evaluate() {
        return this;
    }

    @Override
    public String toString() {
        return value().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Value<?> value1) {
            return value.equals(value1.value());
        }

        return super.equals(obj);
    }

    public abstract ValueType<?> getType();

    private static final Codec<ValueType<?>> VALUE_TYPE_CODEC = ValueType.REGISTRY.getCodec();
    public static final Codec<Value<?>> CODEC = VALUE_TYPE_CODEC.dispatch("type", Value::getType, ValueType::codec);
}
