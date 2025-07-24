package com.braydenoneal.data.controller.function2;

import com.braydenoneal.data.controller.function2.types.booleanfunction.BooleanFunctionBox;
import com.braydenoneal.data.controller.function2.types.booleanfunction.types.NotFunction;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.JsonOps;

public class Test {
    public static void test() {
        Function test = new BooleanFunctionBox(new NotFunction(Either.right(new NotFunction(Either.left(false)))));
        JsonElement result = Function.CODEC.encodeStart(JsonOps.INSTANCE, test).resultOrPartial().orElseThrow();
        System.out.println(result);
        Function result2 = Function.CODEC.parse(JsonOps.INSTANCE, result).resultOrPartial().orElseThrow();
        System.out.println(result2);
    }
}
