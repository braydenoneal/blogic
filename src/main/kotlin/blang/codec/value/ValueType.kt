package blang.codec.value

import blang.expression.value.BlockValue
import blang.expression.value.ItemStackValue
import blang.expression.value.ItemValue
import blang.expression.value.TagValue
import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import program.expression.value.BooleanValue
import program.expression.value.FloatValue
import program.expression.value.FunctionValue
import program.expression.value.IntegerValue
import program.expression.value.ListValue
import program.expression.value.NullValue
import program.expression.value.RangeValue
import program.expression.value.StringValue
import program.expression.value.StructValue
import program.expression.value.Value
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

        val REGISTRY: Registry<ValueType<*>> = MappedRegistry(
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("blogic", "value_types")), Lifecycle.stable(),
        )

        val CODEC: Codec<Value<*>> = REGISTRY.byNameCodec().dispatch("type", type, ValueType<*>::codec)
        val MAP_CODEC: MapCodec<Value<*>> = CODEC.fieldOf("value")
    }
}
