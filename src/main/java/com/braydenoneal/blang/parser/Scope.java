package com.braydenoneal.blang.parser;

import com.braydenoneal.blang.parser.expression.value.Value;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    public static final Codec<Map<String, Value<?>>> VARIABLES_CODEC = Codec.unboundedMap(Codec.STRING, Value.CODEC);

    private Map<String, Value<?>> variables;
    private final @Nullable Scope parent;

    public Scope(@Nullable Scope parent) {
        this.parent = parent;
        variables = new HashMap<>();
    }

    public Map<String, Value<?>> variables() {
        return variables;
    }

    public void setVariables(Map<String, Value<?>> variables) {
        this.variables = variables;
    }

    public Value<?> get(String name) {
        Value<?> value = variables.get(name);

        if (value == null && parent != null) {
            return parent.get(name);
        }

        return value;
    }

    private Scope parentWithVariable(String name) {
        if (variables.containsKey(name)) {
            return this;
        }

        return parent != null ? parent.parentWithVariable(name) : null;
    }

    public Value<?> set(String name, Value<?> value) {
        Scope parentWithVariable = parentWithVariable(name);

        if (parentWithVariable != null) {
            return parentWithVariable.variables.put(name, value);
        }

        return variables.put(name, value);
    }
}
