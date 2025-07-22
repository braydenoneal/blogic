package com.braydenoneal.data.controller.functions;

import com.braydenoneal.data.controller.TypeData;
import com.braydenoneal.data.controller.Types;

import java.util.ArrayList;

public class Not extends AbstractFunction {
    public Not(String name, TypeData type, ArrayList<Parameter> parameters) {
        super(name, type, parameters);
    }

    @Override
    public Value call(ArrayList<Parameter> parameters) {
        String input = parameters.getFirst().getValue();
        boolean value = !Boolean.parseBoolean(input);
        return new Value(Types.BOOLEAN, Boolean.toString(value));
    }
}
