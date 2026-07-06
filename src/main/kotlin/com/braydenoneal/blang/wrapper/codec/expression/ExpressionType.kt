package com.braydenoneal.blang.wrapper.codec.expression

import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry
import net.minecraft.util.Identifier
import parser.expression.Expression
import java.util.function.Function

data class ExpressionType<T : Expression>(val codec: MapCodec<T>) {
    companion object {
        val type: Function<in Expression, out ExpressionType<*>> = { expression: Expression ->
            when (expression) {
                else -> throw Exception("Expression type not found")
            }
        }

        val REGISTRY: Registry<ExpressionType<*>> = SimpleRegistry(
            RegistryKey.ofRegistry(Identifier.of("blogic", "expression_types")), Lifecycle.stable()
        )

        val CODEC: Codec<Expression> = REGISTRY.getCodec().dispatch("type", type, ExpressionType<*>::codec)
    }
}
