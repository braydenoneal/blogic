package blang.codec.builtin

import blang.codec.valuebuiltin.ValueBuiltinType
import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import program.expression.builtin.Builtin
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

data class BuiltinType<T : Builtin>(val codec: MapCodec<T>) {
    companion object {
        val types: MutableMap<KClass<*>, BuiltinType<*>> = mutableMapOf()

        fun getType(builtin: Builtin): BuiltinType<*> {
            for ((key, value) in types.entries) {
                if (key.isSuperclassOf(builtin::class)) {
                    return value
                }
            }

            throw Exception("Builtin type not found")
        }

        val REGISTRY: Registry<BuiltinType<*>> = MappedRegistry(
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("blogic", "builtin_types")), Lifecycle.stable(),
        )

        val CODEC: Codec<Builtin> = REGISTRY.byNameCodec().dispatch("type", ::getType, BuiltinType<*>::codec)
        val MAP_CODEC: MapCodec<Builtin> = CODEC.fieldOf("builtin")

        inline fun <reified T : Builtin> register(id: String, codec: MapCodec<T>) {
            val type = BuiltinType(codec)
            types[T::class] = type
            Registry.register(REGISTRY, Identifier.fromNamespaceAndPath("blogic", id), type)
        }

        fun initialize() {
            register("value_builtin", ValueBuiltinType.MAP_CODEC)
            register("absolute_value_builtin", BuiltinCodecs.ABSOLUTE_VALUE_BUILTIN_CODEC)
            register("integer_cast_builtin", BuiltinCodecs.INTEGER_CAST_BUILTIN_CODEC)
            register("float_cast_builtin", BuiltinCodecs.FLOAT_CAST_BUILTIN_CODEC)
            register("string_cast_builtin", BuiltinCodecs.STRING_CAST_BUILTIN_CODEC)
            register("round_builtin", BuiltinCodecs.ROUND_BUILTIN_CODEC)
            register("length_builtin", BuiltinCodecs.LENGTH_BUILTIN_CODEC)
            register("minimum_builtin", BuiltinCodecs.MINIMUM_BUILTIN_CODEC)
            register("maximum_builtin", BuiltinCodecs.MAXIMUM_BUILTIN_CODEC)
            register("range_builtin", BuiltinCodecs.RANGE_BUILTIN_CODEC)
            register("type_builtin", BuiltinCodecs.TYPE_BUILTIN_CODEC)
            register("wait_builtin", BuiltinCodecs.WAIT_BUILTIN_CODEC)
            register("print_builtin", BuiltinCodecs.PRINT_BUILTIN_CODEC)
            register("ceil_builtin", BuiltinCodecs.CEIL_BUILTIN_CODEC)
            register("floor_builtin", BuiltinCodecs.FLOOR_BUILTIN_CODEC)
            register("block_builtin", BuiltinCodecs.BLOCK_BUILTIN_CODEC)
            register("block_item_builtin", BuiltinCodecs.BLOCK_ITEM_BUILTIN_CODEC)
            register("break_block_builtin", BuiltinCodecs.BREAK_BLOCK_BUILTIN_CODEC)
            register("delete_items_builtin", BuiltinCodecs.DELETE_ITEMS_BUILTIN_CODEC)
            register("export_all_items_builtin", BuiltinCodecs.EXPORT_ALL_ITEMS_BUILTIN_CODEC)
            register("get_block_builtin", BuiltinCodecs.GET_BLOCK_BUILTIN_CODEC)
            register("get_item_count_builtin", BuiltinCodecs.GET_ITEM_COUNT_BUILTIN_CODEC)
            register("get_items_builtin", BuiltinCodecs.GET_ITEMS_BUILTIN_CODEC)
            register("item_builtin", BuiltinCodecs.ITEM_BUILTIN_CODEC)
            register("place_block_builtin", BuiltinCodecs.PLACE_BLOCK_BUILTIN_CODEC)
            register("read_item_count_builtin", BuiltinCodecs.READ_ITEM_COUNT_BUILTIN_CODEC)
            register("tag_builtin", BuiltinCodecs.TAG_BUILTIN_CODEC)
            register("tags_builtin", BuiltinCodecs.TAGS_BUILTIN_CODEC)
            register("use_item_builtin", BuiltinCodecs.USE_ITEM_BUILTIN_CODEC)
        }
    }
}
