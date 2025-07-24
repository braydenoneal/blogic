package com.braydenoneal.data.controller.function2.parameter;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ParameterTypes {
//    public static final ParameterType<BooleanParameter> BOOLEAN_PARAMETER = register("boolean", new ParameterType<>(BooleanParameter.CODEC));

    public static <T extends Parameter> ParameterType<T> register(String id, ParameterType<T> parameterType) {
        return Registry.register(ParameterType.REGISTRY, Identifier.of("blogic", id), parameterType);
    }
}
