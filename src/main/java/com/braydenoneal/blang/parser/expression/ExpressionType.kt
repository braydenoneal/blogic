package com.braydenoneal.blang.parser.expression;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public record ExpressionType<T extends Expression>(MapCodec<T> codec) {
    public static final Registry<ExpressionType<?>> REGISTRY = new SimpleRegistry<>(
            RegistryKey.ofRegistry(Identifier.of("blogic", "expression_types")), Lifecycle.stable()
    );
}
