package com.braydenoneal.data.controller.function;

import com.braydenoneal.data.controller.function.types.NotFunction;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FunctionTypes {
    public static final FunctionType<NotFunction> NOT_FUNCTION = register("not", new FunctionType<>(NotFunction.CODEC));

    public static <T extends Function> FunctionType<T> register(String id, FunctionType<T> functionType) {
        return Registry.register(FunctionType.REGISTRY, Identifier.of("blogic", id), functionType);
    }
}
