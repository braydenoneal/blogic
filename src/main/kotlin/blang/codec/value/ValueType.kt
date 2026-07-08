package blang.codec.value

import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
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
                is blang.expression.value.BlockValue -> ValueTypes.BLOCK
                is blang.expression.value.ItemStackValue -> ValueTypes.ITEM_STACK
                is blang.expression.value.ItemValue -> ValueTypes.ITEM
                is blang.expression.value.TagValue -> ValueTypes.TAG
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
