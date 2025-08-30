package com.braydenoneal.blang.parser.expression.value;

import com.braydenoneal.blang.parser.Program;
import com.braydenoneal.blang.parser.expression.Expression;
import com.braydenoneal.blang.parser.expression.ExpressionType;
import com.braydenoneal.blang.parser.expression.ExpressionTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public abstract class Value<T> implements Expression {
    private final T value;

    public Value(T value) {
        this.value = value;
    }

    public T value() {
        return value;
    }

    @Override
    public Value<?> evaluate(Program program) {
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

    @Override
    public ExpressionType<?> getType() {
        return ExpressionTypes.VALUE;
    }

    public abstract ValueType<?> getValueType();

    public static final Codec<Value<?>> CODEC = ValueType.REGISTRY.getCodec().dispatch("type", Value::getValueType, ValueType::codec);
    public static final MapCodec<Value<?>> MAP_CODEC = CODEC.fieldOf("value");
}
