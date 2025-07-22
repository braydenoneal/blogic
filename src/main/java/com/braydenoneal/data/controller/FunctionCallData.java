package com.braydenoneal.data.controller;

import java.util.ArrayList;

public class FunctionCallData {
    private FunctionData function;
    private ArrayList<FunctionCallData> parameters;

    public FunctionCallData(FunctionData function, ArrayList<FunctionCallData> parameters) {
        this.function = function;
        this.parameters = parameters;
    }
}
