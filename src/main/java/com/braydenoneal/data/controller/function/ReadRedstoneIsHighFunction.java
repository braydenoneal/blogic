package com.braydenoneal.data.controller.function;

import com.braydenoneal.Blogic;

import java.util.Map;

public class ReadRedstoneIsHighFunction extends AbstractFunction {
    public ReadRedstoneIsHighFunction(Map<String, AbstractTerminal> parameters) {
        super(parameters);
    }

    @Override
    public AbstractTerminal call() {
        AbstractTerminal xTerminal = parameters.get("x");
        AbstractTerminal yTerminal = parameters.get("y");
        AbstractTerminal zTerminal = parameters.get("z");

        if (xTerminal instanceof IntegerTerminal x && yTerminal instanceof IntegerTerminal y && zTerminal instanceof IntegerTerminal z) {
            Blogic.LOGGER.info("{}, {}, {}", x.getValue(), y.getValue(), z.getValue());
            return new BooleanTerminal(true);
        }

        return new ErrorTerminal();
    }
}
