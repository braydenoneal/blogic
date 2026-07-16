package blang.codec.expression

import blang.expression.builtin.*
import blang.expression.builtin.PrintBuiltin
import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import parser.expression.*
import parser.expression.builtin.*
import parser.expression.builtin.list.*
import parser.expression.builtin.struct.StructEntriesBuiltin
import parser.expression.builtin.struct.StructKeysBuiltin
import parser.expression.builtin.struct.StructRemoveBuiltin
import parser.expression.builtin.struct.StructValuesBuiltin
import parser.expression.operator.ArithmeticOperator
import parser.expression.operator.BangOperator
import parser.expression.operator.BooleanOperator
import parser.expression.operator.ComparisonOperator
import parser.expression.value.Value
import java.util.function.Function

data class ExpressionType<T : Expression>(val codec: MapCodec<T>) {
    companion object {
        val type: Function<in Expression, out ExpressionType<*>> = { expression: Expression ->
            when (expression) {
                is Value<*> -> ExpressionTypes.VALUE
                is AssignmentExpression -> ExpressionTypes.ASSIGNMENT_EXPRESSION
                is CallExpression -> ExpressionTypes.CALL_EXPRESSION
                is IfElseExpression -> ExpressionTypes.IF_ELSE_EXPRESSION
                is ListExpression -> ExpressionTypes.LIST_EXPRESSION
                is StructExpression -> ExpressionTypes.STRUCT_EXPRESSION
                is ListAccessExpression -> ExpressionTypes.LIST_ACCESS_EXPRESSION
                is MemberExpression -> ExpressionTypes.MEMBER_EXPRESSION
                is MemberCallExpression -> ExpressionTypes.MEMBER_CALL_EXPRESSION
                is VariableExpression -> ExpressionTypes.VARIABLE_EXPRESSION
                is ArithmeticOperator -> ExpressionTypes.ARITHMETIC_OPERATOR
                is BooleanOperator -> ExpressionTypes.BOOLEAN_OPERATOR
                is ComparisonOperator -> ExpressionTypes.COMPARISON_OPERATOR
                is BangOperator -> ExpressionTypes.UNARY_OPERATOR
                is AbsoluteValueBuiltin -> ExpressionTypes.ABSOLUTE_VALUE_BUILTIN
                is IntegerCastBuiltin -> ExpressionTypes.INTEGER_CAST_BUILTIN
                is FloatCastBuiltin -> ExpressionTypes.FLOAT_CAST_BUILTIN
                is StringCastBuiltin -> ExpressionTypes.STRING_CAST_BUILTIN
                is RoundBuiltin -> ExpressionTypes.ROUND_BUILTIN
                is LengthBuiltin -> ExpressionTypes.LENGTH_BUILTIN
                is MinimumBuiltin -> ExpressionTypes.MINIMUM_BUILTIN
                is MaximumBuiltin -> ExpressionTypes.MAXIMUM_BUILTIN
                is RangeBuiltin -> ExpressionTypes.RANGE_BUILTIN
                is ListAppendBuiltin -> ExpressionTypes.LIST_APPEND_BUILTIN
                is ListInsertBuiltin -> ExpressionTypes.LIST_INSERT_BUILTIN
                is ListRemoveBuiltin -> ExpressionTypes.LIST_REMOVE_BUILTIN
                is ListPopBuiltin -> ExpressionTypes.LIST_POP_BUILTIN
                is ListContainsBuiltin -> ExpressionTypes.LIST_CONTAINS_BUILTIN
                is ListContainsAllBuiltin -> ExpressionTypes.LIST_CONTAINS_ALL_BUILTIN
                is StructRemoveBuiltin -> ExpressionTypes.STRUCT_REMOVE_BUILTIN
                is StructKeysBuiltin -> ExpressionTypes.STRUCT_KEYS_BUILTIN
                is StructValuesBuiltin -> ExpressionTypes.STRUCT_VALUES_BUILTIN
                is StructEntriesBuiltin -> ExpressionTypes.STRUCT_ENTRIES_BUILTIN
                is TypeBuiltin -> ExpressionTypes.TYPE_BUILTIN
                is WaitBuiltin -> ExpressionTypes.WAIT_BUILTIN
                is PrintBuiltin -> ExpressionTypes.PRINT_BUILTIN
                is CeilBuiltin -> ExpressionTypes.CEIL_BUILTIN
                is FloorBuiltin -> ExpressionTypes.FLOOR_BUILTIN
                is BlockBuiltin -> ExpressionTypes.BLOCK_BUILTIN
                is BlockItemBuiltin -> ExpressionTypes.BLOCK_ITEM_BUILTIN
                is BreakBlockBuiltin -> ExpressionTypes.BREAK_BLOCK_BUILTIN
                is DeleteItemsBuiltin -> ExpressionTypes.DELETE_ITEMS_BUILTIN
                is ExportAllItemsBuiltin -> ExpressionTypes.EXPORT_ALL_ITEMS_BUILTIN
                is GetBlockBuiltin -> ExpressionTypes.GET_BLOCK_BUILTIN
                is GetItemCountBuiltin -> ExpressionTypes.GET_ITEM_COUNT_BUILTIN
                is GetItemsBuiltin -> ExpressionTypes.GET_ITEMS_BUILTIN
                is ItemBuiltin -> ExpressionTypes.ITEM_BUILTIN
                is PlaceBlockBuiltin -> ExpressionTypes.PLACE_BLOCK_BUILTIN
                is ReadItemCountBuiltin -> ExpressionTypes.READ_ITEM_COUNT_BUILTIN
                is TagBuiltin -> ExpressionTypes.TAG_BUILTIN
                is TagsBuiltin -> ExpressionTypes.TAGS_BUILTIN
                is UseItemBuiltin -> ExpressionTypes.USE_ITEM_BUILTIN
                else -> throw Exception("Expression type not found")
            }
        }

        val REGISTRY: Registry<ExpressionType<*>> = MappedRegistry(
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("blogic", "expression_types")), Lifecycle.stable(),
        )

        val CODEC: Codec<Expression> = REGISTRY.byNameCodec().dispatch("type", type, ExpressionType<*>::codec)
    }
}
