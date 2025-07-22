package com.braydenoneal.data.controller;

import java.util.ArrayList;

public class FileData {
    private String name;
    private ArrayList<FunctionData> functions;

    public FileData(String name, ArrayList<FunctionData> functions) {
        this.name = name;
        this.functions = functions;
    }
}
