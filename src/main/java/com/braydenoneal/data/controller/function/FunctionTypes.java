package com.braydenoneal.data.controller.function;

import com.braydenoneal.data.controller.function.types.*;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FunctionTypes {
    public static final FunctionType<GetVariableFunction> GET_VARIABLE_FUNCTION = register("get_variable", new FunctionType<>(GetVariableFunction.CODEC));
    public static final FunctionType<SetVariableFunction> SET_VARIABLE_FUNCTION = register("set_variable", new FunctionType<>(SetVariableFunction.CODEC));
    public static final FunctionType<GetGlobalVariableFunction> GET_GLOBAL_VARIABLE_FUNCTION = register("get_global_variable", new FunctionType<>(GetGlobalVariableFunction.CODEC));
    public static final FunctionType<SetGlobalVariableFunction> SET_GLOBAL_VARIABLE_FUNCTION = register("set_global_variable", new FunctionType<>(SetGlobalVariableFunction.CODEC));
    public static final FunctionType<NotFunction> NOT_FUNCTION = register("not", new FunctionType<>(NotFunction.CODEC));
    public static final FunctionType<ReadRedstoneFunction> READ_REDSTONE_FUNCTION = register("read_redstone", new FunctionType<>(ReadRedstoneFunction.CODEC));
    public static final FunctionType<IsBlockAirFunction> IS_BLOCK_AIR_FUNCTION = register("is_block_air", new FunctionType<>(IsBlockAirFunction.CODEC));
    public static final FunctionType<PlaceBlockFunction> PLACE_BLOCK_FUNCTION = register("place_block", new FunctionType<>(PlaceBlockFunction.CODEC));
    public static final FunctionType<AddFunction> ADD_FUNCTION = register("add", new FunctionType<>(AddFunction.CODEC));
    public static final FunctionType<LessThanFunction> LESS_THAN_FUNCTION = register("less_than", new FunctionType<>(LessThanFunction.CODEC));
    public static final FunctionType<GreaterThanFunction> GREATER_THAN_FUNCTION = register("greater_than", new FunctionType<>(GreaterThanFunction.CODEC));
    public static final FunctionType<EqualsFunction> EQUALS_FUNCTION = register("equals", new FunctionType<>(EqualsFunction.CODEC));
    public static final FunctionType<ConditionalFunction> CONDITIONAL_FUNCTION = register("conditional", new FunctionType<>(ConditionalFunction.CODEC));
    public static final FunctionType<AndFunction> AND_FUNCTION = register("and", new FunctionType<>(AndFunction.CODEC));
    public static final FunctionType<OrFunction> OR_FUNCTION = register("or", new FunctionType<>(OrFunction.CODEC));
    public static final FunctionType<ModulusFunction> MODULUS_FUNCTION = register("modulus", new FunctionType<>(ModulusFunction.CODEC));
    public static final FunctionType<GetTimeFunction> GET_TIME_FUNCTION = register("get_time", new FunctionType<>(GetTimeFunction.CODEC));

    public static <T extends Function> FunctionType<T> register(String id, FunctionType<T> functionType) {
        return Registry.register(FunctionType.REGISTRY, Identifier.of("blogic", id), functionType);
    }

    public static void initialize() {
    }
}
