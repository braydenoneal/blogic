package com.braydenoneal.data.controller.function2;

import java.util.Map;

public abstract class AbstractFunction {
    protected final Map<String, AbstractTerminal> parameters;

    public AbstractFunction(Map<String, AbstractTerminal> parameters) {
        this.parameters = parameters;
    }

    public abstract AbstractTerminal call();
}
