package com.braydenoneal.blang.parser.expression

import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry
import net.minecraft.util.Identifier

data class ExpressionType<T : Expression>(val codec: MapCodec<T>) {
    companion object {
        val REGISTRY: Registry<ExpressionType<*>> = SimpleRegistry(
            RegistryKey.ofRegistry(Identifier.of("blogic", "expression_types")), Lifecycle.stable()
        )
    }
}
