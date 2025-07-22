package com.braydenoneal.data.controller;

import java.util.ArrayList;

public class FunctionData {
    private String name;
    private TypeData type;
    private ArrayList<ParameterData> parameters;
    private ArrayList<FunctionCallData> body;

    public FunctionData(String name, TypeData type, ArrayList<ParameterData> parameters, ArrayList<FunctionCallData> body) {
        this.name = name;
        this.type = type;
        this.parameters = parameters;
        this.body = body;
    }
}
