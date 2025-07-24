package com.braydenoneal.data.controller.function2.parameter;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public record ParameterType<T extends Parameter>(MapCodec<T> codec) {
    public static final Registry<ParameterType<?>> REGISTRY = new SimpleRegistry<>(
            RegistryKey.ofRegistry(Identifier.of("blogic", "parameter_types")), Lifecycle.stable()
    );
}
