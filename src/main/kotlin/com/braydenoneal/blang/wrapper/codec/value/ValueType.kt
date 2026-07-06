package com.braydenoneal.blang.wrapper.codec.value

import com.braydenoneal.blang.wrapper.expression.value.BlockValue
import com.braydenoneal.blang.wrapper.expression.value.ItemStackValue
import com.braydenoneal.blang.wrapper.expression.value.ItemValue
import com.braydenoneal.blang.wrapper.expression.value.TagValue
import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry
import net.minecraft.util.Identifier
import parser.expression.value.*
import java.util.function.Function

data class ValueType<T : Value<*>>(val codec: MapCodec<T>) {
    companion object {
        val type: Function<in Value<*>, out ValueType<*>> = { value: Value<*> ->
            when (value) {
                is BooleanValue -> ValueTypes.BOOLEAN
                is FloatValue -> ValueTypes.FLOAT
                is FunctionValue -> ValueTypes.FUNCTION
                is IntegerValue -> ValueTypes.INTEGER
                is ListValue -> ValueTypes.LIST
                is NullValue -> ValueTypes.NULL
                is RangeValue -> ValueTypes.RANGE
                is StringValue -> ValueTypes.STRING
                is StructValue -> ValueTypes.STRUCT
                is BlockValue -> ValueTypes.BLOCK
                is ItemStackValue -> ValueTypes.ITEM_STACK
                is ItemValue -> ValueTypes.ITEM
                is TagValue -> ValueTypes.TAG
                else -> throw Exception("Statement type not found")
            }
        }

        val REGISTRY: Registry<ValueType<*>> = SimpleRegistry(
            RegistryKey.ofRegistry(Identifier.of("blogic", "value_types")), Lifecycle.stable(),
        )

        val CODEC: Codec<Value<*>> = REGISTRY.getCodec().dispatch("type", type, ValueType<*>::codec)
        val MAP_CODEC: MapCodec<Value<*>> = CODEC.fieldOf("value")
    }
}
