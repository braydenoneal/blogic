package com.braydenoneal.data.controller.function;

import java.util.Map;

public abstract class AbstractFunction extends AbstractParameter {
    protected final Map<String, AbstractParameter> parameters;

    public AbstractFunction(Map<String, AbstractParameter> parameters) {
        this.parameters = parameters;
    }

    public abstract AbstractTerminal call();
}
