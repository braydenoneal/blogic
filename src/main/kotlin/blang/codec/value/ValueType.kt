package blang.codec.value

import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import program.expression.value.Value
import java.util.function.Function
import kotlin.reflect.KClass

data class ValueType<T : Value<*>>(val codec: MapCodec<T>) {
    companion object {
        val types: MutableMap<KClass<*>, ValueType<*>> = mutableMapOf()

        val type: Function<in Value<*>, out ValueType<*>> = { value: Value<*> ->
            types[value::class] ?: throw Exception("Value type not found")
        }

        val REGISTRY: Registry<ValueType<*>> = MappedRegistry(
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("blogic", "value_types")), Lifecycle.stable(),
        )

        val CODEC: Codec<Value<*>> = REGISTRY.byNameCodec().dispatch("type", type, ValueType<*>::codec)
        val MAP_CODEC: MapCodec<Value<*>> = CODEC.fieldOf("value")

        inline fun <reified T : Value<*>> register(id: String, codec: MapCodec<T>) {
            val type = ValueType(codec)
            types[T::class] = type
            Registry.register(REGISTRY, Identifier.fromNamespaceAndPath("blogic", id), type)
        }

        fun initialize() {
            register("boolean", ValueCodecs.BOOLEAN_VALUE_CODEC)
            register("float", ValueCodecs.FLOAT_VALUE_CODEC)
            register("function", ValueCodecs.FUNCTION_VALUE_CODEC)
            register("integer", ValueCodecs.INTEGER_VALUE_CODEC)
            register("list", ValueCodecs.LIST_VALUE_CODEC)
            register("null", ValueCodecs.NULL_VALUE_CODEC)
            register("range", ValueCodecs.RANGE_VALUE_CODEC)
            register("string", ValueCodecs.STRING_VALUE_CODEC)
            register("struct", ValueCodecs.STRUCT_VALUE_CODEC)
            register("block", ValueCodecs.BLOCK_VALUE_CODEC)
            register("item_stack", ValueCodecs.ITEM_STACK_CODEC)
            register("item", ValueCodecs.ITEM_CODEC)
            register("tag", ValueCodecs.TAG_CODEC)
        }
    }
}
