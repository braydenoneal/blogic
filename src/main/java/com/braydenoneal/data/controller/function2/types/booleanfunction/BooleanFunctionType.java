package com.braydenoneal.data.controller.function2.types.booleanfunction;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public record BooleanFunctionType<T extends BooleanFunction>(MapCodec<T> codec) {
    public static final Registry<BooleanFunctionType<?>> REGISTRY = new SimpleRegistry<>(
            RegistryKey.ofRegistry(Identifier.of("blogic", "boolean_function_types")), Lifecycle.stable()
    );
}
