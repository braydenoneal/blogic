package com.braydenoneal.data.functionold;

public class BooleanTerminal extends AbstractTerminal {
    private final boolean value;

    public BooleanTerminal(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
