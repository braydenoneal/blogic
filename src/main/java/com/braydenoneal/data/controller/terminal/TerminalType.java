package com.braydenoneal.data.controller.terminal;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public record TerminalType<T extends Terminal>(MapCodec<T> codec) {
    public static final Registry<TerminalType<?>> REGISTRY = new SimpleRegistry<>(
            RegistryKey.ofRegistry(Identifier.of("blogic", "terminal_types")), Lifecycle.stable()
    );
}
