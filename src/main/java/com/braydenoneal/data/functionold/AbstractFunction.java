package com.braydenoneal.data.functionold;

import java.util.Map;

public abstract class AbstractFunction extends AbstractParameter {
    protected final Map<String, AbstractParameter> parameters;
    protected final String name = "abstract";

    public AbstractFunction(Map<String, AbstractParameter> parameters) {
        this.parameters = parameters;
    }

    public Map<String, AbstractParameter> getParameters() {
        return parameters;
    }

    public String getName() {
        return name;
    }

    public abstract AbstractTerminal call();
}
