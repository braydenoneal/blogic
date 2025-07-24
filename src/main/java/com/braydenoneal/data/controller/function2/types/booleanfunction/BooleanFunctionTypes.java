package com.braydenoneal.data.controller.function2.types.booleanfunction;

import com.braydenoneal.data.controller.function2.types.booleanfunction.types.NotFunction;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BooleanFunctionTypes {
    public static final BooleanFunctionType<NotFunction> NOT_FUNCTION = register("not", new BooleanFunctionType<>(NotFunction.CODEC));

    public static <T extends BooleanFunction> BooleanFunctionType<T> register(String id, BooleanFunctionType<T> booleanFunctionType) {
        return Registry.register(BooleanFunctionType.REGISTRY, Identifier.of("blogic", id), booleanFunctionType);
    }
}
