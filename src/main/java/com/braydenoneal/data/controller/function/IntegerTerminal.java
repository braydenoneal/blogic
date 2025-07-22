package com.braydenoneal.data.controller.function;

public class IntegerTerminal extends AbstractTerminal {
    private final int value;

    public IntegerTerminal(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
