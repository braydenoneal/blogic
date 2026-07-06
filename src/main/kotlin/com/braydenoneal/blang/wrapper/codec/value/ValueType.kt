package com.braydenoneal.blang.wrapper.codec.value

import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry
import net.minecraft.util.Identifier
import parser.expression.value.Value
import java.util.function.Function

data class ValueType<T : Value<*>>(val codec: MapCodec<T>) {
    companion object {
        val type: Function<in Value<*>, out ValueType<*>> = { value: Value<*> ->
            when (value) {
                else -> throw Exception("Statement type not found")
            }
        }

        val REGISTRY: Registry<ValueType<*>> = SimpleRegistry(
            RegistryKey.ofRegistry(Identifier.of("blogic", "value_types")), Lifecycle.stable()
        )

        val CODEC: Codec<Value<*>> = REGISTRY.getCodec().dispatch("type", type, ValueType<*>::codec)
        val MAP_CODEC: MapCodec<Value<*>> = CODEC.fieldOf("value")
    }
}
