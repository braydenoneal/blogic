package com.braydenoneal.data.controller.parameter;

import com.braydenoneal.data.controller.parameter.types.BooleanParameter;
import com.braydenoneal.data.controller.parameter.types.IntegerParameter;
import com.braydenoneal.data.controller.parameter.types.ListParameter;
import com.braydenoneal.data.controller.parameter.types.VoidParameter;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ParameterTypes {
    public static final ParameterType<BooleanParameter> BOOLEAN_PARAMETER = register("boolean", new ParameterType<>(BooleanParameter.CODEC));
    public static final ParameterType<IntegerParameter> INTEGER_PARAMETER = register("integer", new ParameterType<>(IntegerParameter.CODEC));
    public static final ParameterType<VoidParameter> VOID_PARAMETER = register("void", new ParameterType<>(VoidParameter.CODEC));
    public static final ParameterType<ListParameter> LIST_PARAMETER = register("list", new ParameterType<>(ListParameter.CODEC));

    public static <T extends Parameter> ParameterType<T> register(String id, ParameterType<T> parameterType) {
        return Registry.register(ParameterType.REGISTRY, Identifier.of("blogic", id), parameterType);
    }

    public static void initialize() {
    }
}
