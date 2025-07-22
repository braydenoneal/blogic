package com.braydenoneal.data.controller;

import com.braydenoneal.data.controller.function.Function;
import com.braydenoneal.data.controller.terminal.BoolTerminal;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;

public class TEST {
    public static void main(String[] args) {
        Function test = new Function(Either.left(new Function(Either.right(new BoolTerminal(false)))));
        DataResult<JsonElement> result = Function.codec.encodeStart(JsonOps.INSTANCE, test);
        System.out.println(result);
    }
}
