package com.braydenoneal.data.controller.function;

import java.util.Map;

public class NotFunction extends AbstractFunction {
    protected final String name = "not";

    public NotFunction(Map<String, AbstractParameter> parameters) {
        super(parameters);
    }

    @Override
    public AbstractTerminal call() {
        AbstractParameter input = parameters.get("input");

        if (input instanceof BooleanTerminal inputTerminal) {
            return new BooleanTerminal(!inputTerminal.getValue());
        }

        return new ErrorTerminal();
    }
}
