package com.braydenoneal.data.controller.functions;

import com.braydenoneal.data.controller.TypeData;

public class Parameter {
    private String name;
    private TypeData type;
    private String value;

    public Parameter(String name, TypeData type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
