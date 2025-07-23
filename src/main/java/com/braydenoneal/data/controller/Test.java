package com.braydenoneal.data.controller;

import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.types.NotFunction;
import com.braydenoneal.data.controller.terminal.types.BooleanTerminal;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.JsonOps;

public class Test {
    public static void test() {
        NotFunction test = new NotFunction(
                Either.right(new NotFunction(
                        Either.left(new BooleanTerminal(false)))
                )
        );
        JsonElement result = Function.CODEC.encodeStart(JsonOps.INSTANCE, test).resultOrPartial().orElseThrow();
        System.out.println(result);
        Function result2 = Function.CODEC.parse(JsonOps.INSTANCE, result).resultOrPartial().orElseThrow();
        System.out.println(result2);

        JsonElement fromString = JsonParser.parseString("{\"input\":{\"input\":{\"value\":false,\"terminalType\":\"blogic:bool\"},\"functionType\":\"blogic:not\"},\"functionType\":\"blogic:not\"}");
        Function test2 = Function.CODEC.parse(JsonOps.INSTANCE, fromString).resultOrPartial().orElseThrow();
        System.out.println(test2.call());
    }
}
