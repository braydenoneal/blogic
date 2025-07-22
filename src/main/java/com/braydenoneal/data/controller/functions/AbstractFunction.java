package com.braydenoneal.data.controller.functions;

import com.braydenoneal.data.controller.TypeData;

import java.util.ArrayList;

public abstract class AbstractFunction {
    private String name;
    private TypeData type;
    private ArrayList<Parameter> parameters;

    public AbstractFunction(String name, TypeData type, ArrayList<Parameter> parameters) {
        this.name = name;
        this.type = type;
        this.parameters = parameters;
    }

    public abstract Value call(ArrayList<Parameter> parameters);
}
