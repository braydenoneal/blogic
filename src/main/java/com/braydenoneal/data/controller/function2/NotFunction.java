package com.braydenoneal.data.controller.function2;

import java.util.Map;

public class NotFunction extends AbstractFunction {
    public NotFunction(Map<String, AbstractTerminal> parameters) {
        super(parameters);
    }

    @Override
    public AbstractTerminal call() {
        AbstractTerminal input = parameters.get("input");

        if (input instanceof BooleanTerminal inputTerminal) {
            return new BooleanTerminal(!inputTerminal.getValue());
        }

        return new ErrorTerminal();
    }
}
