package com.braydenoneal.blang.parser.statement;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public record StatementType<T extends Statement>(MapCodec<T> codec) {
    public static final Registry<StatementType<?>> REGISTRY = new SimpleRegistry<>(
            RegistryKey.ofRegistry(Identifier.of("blogic", "statement_types")), Lifecycle.stable()
    );
}
