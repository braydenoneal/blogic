package com.braydenoneal.data.controller.function2;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

import java.util.List;

public class Parameters {
    public static final Registry<Class<?>> REGISTRY = new SimpleRegistry<>(
            RegistryKey.ofRegistry(Identifier.of("blogic", "parameter_types")), Lifecycle.stable()
    );

    public static final Codec<Class<?>> CODEC = REGISTRY.getCodec();

    public static final Class<?> BOOLEAN = register("boolean", Boolean.class);
    public static final Class<?> INTEGER = register("integer", Integer.class);
    public static final Class<?> LIST = register("list", List.class);

    public static Class<?> register(String id, Class<?> type) {
        return Registry.register(REGISTRY, Identifier.of("blogic", id), type);
    }
}
