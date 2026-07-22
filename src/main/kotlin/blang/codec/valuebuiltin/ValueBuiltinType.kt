package blang.codec.valuebuiltin

import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import program.expression.builtin.ValueBuiltin
import java.util.function.Function
import kotlin.reflect.KClass

data class ValueBuiltinType<T : ValueBuiltin<*>>(val codec: MapCodec<T>) {
    companion object {
        val types: MutableMap<KClass<*>, ValueBuiltinType<*>> = mutableMapOf()

        val type: Function<in ValueBuiltin<*>, out ValueBuiltinType<*>> = { builtin: ValueBuiltin<*> ->
            types[builtin::class] ?: throw Exception("Value builtin type not found")
        }

        val REGISTRY: Registry<ValueBuiltinType<*>> = MappedRegistry(
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("blogic", "value_builtin_types")), Lifecycle.stable(),
        )

        val CODEC: Codec<ValueBuiltin<*>> = REGISTRY.byNameCodec().dispatch("type", type, ValueBuiltinType<*>::codec)
        val MAP_CODEC: MapCodec<ValueBuiltin<*>> = CODEC.fieldOf("value_builtin")

        inline fun <reified T : ValueBuiltin<*>> register(id: String, codec: MapCodec<T>) {
            val type = ValueBuiltinType(codec)
            types[T::class] = type
            Registry.register(REGISTRY, Identifier.fromNamespaceAndPath("blogic", id), type)
        }

        fun initialize() {
            register("list_append_builtin", ValueBuiltinCodecs.LIST_APPEND_BUILTIN_CODEC)
            register("list_insert_builtin", ValueBuiltinCodecs.LIST_INSERT_BUILTIN_CODEC)
            register("list_remove_builtin", ValueBuiltinCodecs.LIST_REMOVE_BUILTIN_CODEC)
            register("list_pop_builtin", ValueBuiltinCodecs.LIST_POP_BUILTIN_CODEC)
            register("list_contains_builtin", ValueBuiltinCodecs.LIST_CONTAINS_BUILTIN_CODEC)
            register("list_contains_all_builtin", ValueBuiltinCodecs.LIST_CONTAINS_ALL_BUILTIN_CODEC)
            register("struct_remove_builtin", ValueBuiltinCodecs.STRUCT_REMOVE_BUILTIN_CODEC)
            register("struct_keys_builtin", ValueBuiltinCodecs.STRUCT_KEYS_BUILTIN_CODEC)
            register("struct_values_builtin", ValueBuiltinCodecs.STRUCT_VALUES_BUILTIN_CODEC)
            register("struct_entries_builtin", ValueBuiltinCodecs.STRUCT_ENTRIES_BUILTIN_CODEC)
        }
    }
}
