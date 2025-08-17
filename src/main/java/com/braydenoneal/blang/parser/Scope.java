package com.braydenoneal.blang.parser;

import com.braydenoneal.blang.parser.expression.value.Value;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    private final Map<String, Value<?>> variables;
    private final @Nullable Scope parent;

    public Scope(@Nullable Scope parent) {
        this.parent = parent;
        variables = new HashMap<>();
    }

    public Value<?> get(String name) {
        Value<?> value = variables.get(name);

        if (value == null && parent != null) {
            return parent.get(name);
        }

        return value;
    }

    public void set(String name, Value<?> value) {
        variables.put(name, value);
    }
}
