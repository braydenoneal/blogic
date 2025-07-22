package com.braydenoneal.data.controller.function;

import java.util.Map;

public class FunctionMap {
    public static final Map<String, Class<? extends AbstractFunction>> functionMap = Map.of(
            "not", NotFunction.class,
            "readRedstoneIsHigh", ReadRedstoneIsHighFunction.class,
            "writeRedstoneIsHigh", WriteRedstoneIsHighFunction.class
    );
}
