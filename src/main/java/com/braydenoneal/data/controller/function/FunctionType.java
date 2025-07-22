package com.braydenoneal.data.controller.function;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public record FunctionType<T extends Function>(MapCodec<T> codec) {
    public static final Registry<FunctionType<?>> REGISTRY = new SimpleRegistry<>(
            RegistryKey.ofRegistry(Identifier.of("blogic", "function_types")), Lifecycle.stable()
    );
}
