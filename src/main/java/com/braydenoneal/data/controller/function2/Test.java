package com.braydenoneal.data.controller.function2;

import com.braydenoneal.data.controller.function2.types.booleanfunction.BooleanFunctionBox;
import com.braydenoneal.data.controller.function2.types.booleanfunction.types.NotFunction;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

import java.util.Map;

public class Test {
    public static void test() {
        Map<String, Class<?>> parameters = Map.of(
                "x", Integer.class,
                "y", Integer.class,
                "z", Integer.class,
                "value", Boolean.class
        );

        JsonElement result0 = Codec.unboundedMap(Codec.STRING, Parameters.CODEC).encodeStart(JsonOps.INSTANCE, parameters).resultOrPartial().orElseThrow();
        System.out.println(result0);
        Map<String, Class<?>> result3 = Codec.unboundedMap(Codec.STRING, Parameters.CODEC).parse(JsonOps.INSTANCE, result0).resultOrPartial().orElseThrow();
        System.out.println(result3);
        System.exit(0);

        Function test = new BooleanFunctionBox(new NotFunction(Either.right(new NotFunction(Either.left(false)))));
        JsonElement result = Function.CODEC.encodeStart(JsonOps.INSTANCE, test).resultOrPartial().orElseThrow();
        System.out.println(result);
        Function result2 = Function.CODEC.parse(JsonOps.INSTANCE, result).resultOrPartial().orElseThrow();
        System.out.println(result2);
    }
}
