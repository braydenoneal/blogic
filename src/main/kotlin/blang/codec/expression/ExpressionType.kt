package blang.codec.expression

import blang.codec.builtin.BuiltinType
import blang.codec.value.ValueType
import blang.codec.valuebuiltin.ValueBuiltinType
import blang.expression.builtin.*
import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import parser.expression.BuiltinExpressionParser
import program.expression.Expression
import program.expression.builtin.Builtin
import program.expression.builtin.ValueBuiltin
import program.expression.value.Value
import kotlin.reflect.KClass

data class ExpressionType<T : Expression>(val codec: MapCodec<T>) {
    companion object {
        val types: MutableMap<KClass<*>, ExpressionType<*>> = mutableMapOf()

        fun getType(expression: Expression): ExpressionType<*> {
            return when (expression) {
                is Value<*> -> types[Value::class]!!
                is ValueBuiltin<*> -> types[ValueBuiltin::class]!!
                is Builtin -> types[Builtin::class]!!
                else -> types[expression::class] ?: throw Exception("Expression type not found")
            }
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
            register("value", ValueType.MAP_CODEC)
            register("value_builtin", ValueBuiltinType.MAP_CODEC)
            register("builtin", BuiltinType.MAP_CODEC)
            register("assign_expression", ExpressionCodecs.ASSIGN_EXPRESSION_CODEC)
            register("call_expression", ExpressionCodecs.CALL_EXPRESSION_CODEC)
            register("if_else_expression", ExpressionCodecs.IF_ELSE_EXPRESSION_CODEC)
            register("list_expression", ExpressionCodecs.LIST_EXPRESSION_CODEC)
            register("struct_expression", ExpressionCodecs.STRUCT_EXPRESSION_CODEC)
            register("access_expression", ExpressionCodecs.ACCESS_EXPRESSION_CODEC)
            register("member_expression", ExpressionCodecs.DOT_EXPRESSION_CODEC)
            register("identifier_expression", ExpressionCodecs.IDENTIFIER_EXPRESSION_CODEC)
            register("binary_operator_expression", ExpressionCodecs.BINARY_OPERATOR_EXPRESSION_CODEC)
            register("unary_operator_expression", ExpressionCodecs.UNARY_OPERATOR_EXPRESSION_CODEC)
            BuiltinExpressionParser.register("print", ::PrintBuiltin)
            BuiltinExpressionParser.register("block", ::BlockBuiltin)
            BuiltinExpressionParser.register("blockItem", ::BlockItemBuiltin)
            BuiltinExpressionParser.register("breakBlock", ::BreakBlockBuiltin)
            BuiltinExpressionParser.register("deleteItems", ::DeleteItemsBuiltin)
            BuiltinExpressionParser.register("exportAllItems", ::ExportAllItemsBuiltin)
            BuiltinExpressionParser.register("getBlock", ::GetBlockBuiltin)
            BuiltinExpressionParser.register("getItemCount", ::GetItemCountBuiltin)
            BuiltinExpressionParser.register("getItems", ::GetItemsBuiltin)
            BuiltinExpressionParser.register("item", ::ItemBuiltin)
            BuiltinExpressionParser.register("placeBlock", ::PlaceBlockBuiltin)
            BuiltinExpressionParser.register("readItemCount", ::ReadItemCountBuiltin)
            BuiltinExpressionParser.register("tag", ::TagBuiltin)
            BuiltinExpressionParser.register("tags", ::TagsBuiltin)
            BuiltinExpressionParser.register("useItem", ::UseItemBuiltin)
        }
    }
}
