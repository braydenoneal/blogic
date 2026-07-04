package com.braydenoneal.blang.parser.expression.value

import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry
import net.minecraft.util.Identifier

data class ValueType<T : Value<*>>(val codec: MapCodec<T>) {
    companion object {
        val REGISTRY: Registry<ValueType<*>> = SimpleRegistry(
            RegistryKey.ofRegistry(Identifier.of("blogic", "value_types")), Lifecycle.stable()
        )
    }
}
