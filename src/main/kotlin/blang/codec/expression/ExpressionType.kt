package blang.codec.expression

import blang.codec.value.ValueType
import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import program.expression.Expression
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

data class ExpressionType<T : Expression>(val codec: MapCodec<T>) {
    companion object {
        val types: MutableMap<KClass<*>, ExpressionType<*>> = mutableMapOf()

        fun getType(expression: Expression): ExpressionType<*> {
            for ((key, value) in types.entries) {
                if (key.isSuperclassOf(expression::class)) {
                    return value
                }
            }

            throw Exception("Expression type not found")
        }

        val REGISTRY: Registry<ExpressionType<*>> = MappedRegistry(
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("blogic", "expression_types")), Lifecycle.stable(),
        )

        val CODEC: Codec<Expression> = REGISTRY.byNameCodec().dispatch("type", ::getType, ExpressionType<*>::codec)

        inline fun <reified T : Expression> register(id: String, codec: MapCodec<T>) {
            val type = ExpressionType(codec)
            types[T::class] = type
            Registry.register(REGISTRY, Identifier.fromNamespaceAndPath("blogic", id), type)
        }

        fun initialize() {
            register("access_expression", ExpressionCodecs.ACCESS_EXPRESSION_CODEC)
            register("assign_expression", ExpressionCodecs.ASSIGN_EXPRESSION_CODEC)
            register("binary_operator_expression", ExpressionCodecs.BINARY_OPERATOR_EXPRESSION_CODEC)
            register("call_expression", ExpressionCodecs.CALL_EXPRESSION_CODEC)
            register("dot_expression", ExpressionCodecs.DOT_EXPRESSION_CODEC)
            register("get_expression", ExpressionCodecs.GET_EXPRESSION_CODEC)
            register("identifier_expression", ExpressionCodecs.IDENTIFIER_EXPRESSION_CODEC)
            register("if_else_expression", ExpressionCodecs.IF_ELSE_EXPRESSION_CODEC)
            register("infix_function_expression", ExpressionCodecs.INFIX_FUNCTION_EXPRESSION_CODEC)
            register("list_expression", ExpressionCodecs.LIST_EXPRESSION_CODEC)
            register("map_expression", ExpressionCodecs.MAP_EXPRESSION_CODEC)
            register("string_expression", ExpressionCodecs.STRING_EXPRESSION_CODEC)
            register("slice_expression", ExpressionCodecs.SLICE_EXPRESSION_CODEC)
            register("unary_operator_expression", ExpressionCodecs.UNARY_OPERATOR_EXPRESSION_CODEC)
            register("value", ValueType.MAP_CODEC)
        }
    }
}
