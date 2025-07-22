package com.braydenoneal.data.functionold;

import com.braydenoneal.Blogic;

import java.util.Map;

public class ReadRedstoneIsHighFunction extends AbstractFunction {
    protected final String name = "readRedstoneIsHighFunction";

    public ReadRedstoneIsHighFunction(Map<String, AbstractParameter> parameters) {
        super(parameters);
    }

    @Override
    public AbstractTerminal call() {
        AbstractParameter xTerminal = parameters.get("x");
        AbstractParameter yTerminal = parameters.get("y");
        AbstractParameter zTerminal = parameters.get("z");

        if (xTerminal instanceof IntegerTerminal x && yTerminal instanceof IntegerTerminal y && zTerminal instanceof IntegerTerminal z) {
            Blogic.LOGGER.info("{}, {}, {}", x.getValue(), y.getValue(), z.getValue());
            return new BooleanTerminal(true);
        }

        return new ErrorTerminal();
    }
}
