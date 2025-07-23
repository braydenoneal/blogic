package com.braydenoneal.data.controller;

import com.braydenoneal.data.controller.function.CustomFunction;
import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.function.types.GetVariableFunction;
import com.braydenoneal.data.controller.function.types.NotFunction;
import com.braydenoneal.data.controller.function.types.ReadRedstoneFunction;
import com.braydenoneal.data.controller.function.types.WriteRedstoneFunction;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.BooleanTerminal;
import com.braydenoneal.data.controller.terminal.types.IntegerTerminal;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.JsonOps;

import java.util.List;
import java.util.Map;

public class Test {
    public static void test() {
        Function test = new WriteRedstoneFunction(Either.right(new ReadRedstoneFunction(Either.left(new IntegerTerminal(1)), Either.left(new IntegerTerminal(0)), Either.left(new IntegerTerminal(0)))));
        JsonElement result = Function.CODEC.encodeStart(JsonOps.INSTANCE, test).resultOrPartial().orElseThrow();
        System.out.println(result);
        Function result2 = Function.CODEC.parse(JsonOps.INSTANCE, result).resultOrPartial().orElseThrow();
        System.out.println(result2);

        JsonElement fromString = JsonParser.parseString("{\"input\":{\"input\":{\"name\":false,\"terminalType\":\"blogic:bool\"},\"functionType\":\"blogic:not\"},\"functionType\":\"blogic:not\"}");
        Function test2 = Function.CODEC.parse(JsonOps.INSTANCE, fromString).resultOrPartial().orElseThrow();
        System.out.println(test2.call(null, null, Map.of()));

        NotFunction body = new NotFunction(
                Either.right(new NotFunction(
                        Either.right(new GetVariableFunction("input")))
                )
        );
        CustomFunction c = new CustomFunction("main", BooleanTerminal.class, null, List.of(body));
        try {
            Terminal terminal = c.call(null, null, Map.of("input", Either.left(new BooleanTerminal(false))), Map.of());
            System.out.println(terminal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
