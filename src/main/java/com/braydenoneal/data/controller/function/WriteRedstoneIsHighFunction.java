package com.braydenoneal.data.controller.function;

import com.braydenoneal.Blogic;

import java.util.Map;

public class WriteRedstoneIsHighFunction extends AbstractFunction {
    public WriteRedstoneIsHighFunction(Map<String, AbstractTerminal> parameters) {
        super(parameters);
    }

    @Override
    public AbstractTerminal call() {
        AbstractTerminal xTerminal = parameters.get("x");
        AbstractTerminal yTerminal = parameters.get("y");
        AbstractTerminal zTerminal = parameters.get("z");
        AbstractTerminal highTerminal = parameters.get("high");

        if (xTerminal instanceof IntegerTerminal x &&
                yTerminal instanceof IntegerTerminal y &&
                zTerminal instanceof IntegerTerminal z &&
                highTerminal instanceof BooleanTerminal high
        ) {
            Blogic.LOGGER.info("{}, {}, {}, {}", x.getValue(), y.getValue(), z.getValue(), high.getValue());
            return new VoidTerminal();
        }

        return new ErrorTerminal();
    }
}
