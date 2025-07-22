package com.braydenoneal.data.controller.function;

import com.braydenoneal.Blogic;

import java.util.Map;

public class WriteRedstoneIsHighFunction extends AbstractFunction {
    public WriteRedstoneIsHighFunction(Map<String, AbstractParameter> parameters) {
        super(parameters);
    }

    @Override
    public AbstractTerminal call() {
        AbstractParameter xTerminal = parameters.get("x");
        AbstractParameter yTerminal = parameters.get("y");
        AbstractParameter zTerminal = parameters.get("z");
        AbstractParameter highTerminal = parameters.get("high");

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
