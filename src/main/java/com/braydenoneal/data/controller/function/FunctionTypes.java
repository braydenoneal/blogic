package com.braydenoneal.data.controller.function;

import com.braydenoneal.data.controller.function.types.GetVariableFunction;
import com.braydenoneal.data.controller.function.types.NotFunction;
import com.braydenoneal.data.controller.function.types.ReadRedstoneFunction;
import com.braydenoneal.data.controller.function.types.WriteRedstoneFunction;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FunctionTypes {
    public static final FunctionType<NotFunction> NOT_FUNCTION = register("not", new FunctionType<>(NotFunction.CODEC));
    public static final FunctionType<GetVariableFunction> GET_VARIABLE_FUNCTION = register("get_variable", new FunctionType<>(GetVariableFunction.CODEC));
    public static final FunctionType<ReadRedstoneFunction> READ_REDSTONE_FUNCTION = register("read_redstone", new FunctionType<>(ReadRedstoneFunction.CODEC));
    public static final FunctionType<WriteRedstoneFunction> WRITE_REDSTONE_FUNCTION = register("write_redstone", new FunctionType<>(WriteRedstoneFunction.CODEC));

    public static <T extends Function> FunctionType<T> register(String id, FunctionType<T> functionType) {
        return Registry.register(FunctionType.REGISTRY, Identifier.of("blogic", id), functionType);
    }
}
