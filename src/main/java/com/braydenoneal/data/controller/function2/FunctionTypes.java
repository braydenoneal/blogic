package com.braydenoneal.data.controller.function2;

import com.braydenoneal.data.controller.function2.types.booleanfunction.BooleanFunctionBox;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FunctionTypes {
    public static final FunctionType<BooleanFunctionBox> BOOLEAN_FUNCTION = register("boolean", new FunctionType<>(BooleanFunctionBox.CODEC));

    public static <T extends Function> FunctionType<T> register(String id, FunctionType<T> functionType) {
        return Registry.register(FunctionType.REGISTRY, Identifier.of("blogic", id), functionType);
    }
}
