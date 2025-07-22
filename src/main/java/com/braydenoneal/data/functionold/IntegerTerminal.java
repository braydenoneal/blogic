package com.braydenoneal.data.functionold;

public class IntegerTerminal extends AbstractTerminal {
    private final int value;

    public IntegerTerminal(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
