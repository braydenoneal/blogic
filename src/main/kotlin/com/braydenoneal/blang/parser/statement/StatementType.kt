package com.braydenoneal.blang.parser.statement

import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry
import net.minecraft.util.Identifier

data class StatementType<T : Statement>(val codec: MapCodec<T>) {
    companion object {
        val REGISTRY: Registry<StatementType<*>> = SimpleRegistry(
            RegistryKey.ofRegistry(Identifier.of("blogic", "statement_types")), Lifecycle.stable()
        )
    }
}
