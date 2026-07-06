package blang.codec.expression

import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry
import net.minecraft.util.Identifier
import parser.expression.*
import parser.expression.builtin.*
import parser.expression.builtin.list.*
import parser.expression.builtin.struct.StructEntriesBuiltin
import parser.expression.builtin.struct.StructKeysBuiltin
import parser.expression.builtin.struct.StructRemoveBuiltin
import parser.expression.builtin.struct.StructValuesBuiltin
import parser.expression.operator.ArithmeticOperator
import parser.expression.operator.BooleanOperator
import parser.expression.operator.ComparisonOperator
import parser.expression.operator.UnaryOperator
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
                is NamedListAccessExpression -> ExpressionTypes.NAMED_LIST_ACCESS_EXPRESSION
                is VariableExpression -> ExpressionTypes.VARIABLE_EXPRESSION
                is ArithmeticOperator -> ExpressionTypes.ARITHMETIC_OPERATOR
                is BooleanOperator -> ExpressionTypes.BOOLEAN_OPERATOR
                is ComparisonOperator -> ExpressionTypes.COMPARISON_OPERATOR
                is UnaryOperator -> ExpressionTypes.UNARY_OPERATOR
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
                is blang.expression.builtin.PrintBuiltin -> ExpressionTypes.PRINT_BUILTIN
                is CeilBuiltin -> ExpressionTypes.CEIL_BUILTIN
                is FloorBuiltin -> ExpressionTypes.FLOOR_BUILTIN
                is blang.expression.builtin.BlockBuiltin -> ExpressionTypes.BLOCK_BUILTIN
                is blang.expression.builtin.BlockItemBuiltin -> ExpressionTypes.BLOCK_ITEM_BUILTIN
                is blang.expression.builtin.BreakBlockBuiltin -> ExpressionTypes.BREAK_BLOCK_BUILTIN
                is blang.expression.builtin.DeleteItemsBuiltin -> ExpressionTypes.DELETE_ITEMS_BUILTIN
                is blang.expression.builtin.ExportAllItemsBuiltin -> ExpressionTypes.EXPORT_ALL_ITEMS_BUILTIN
                is blang.expression.builtin.GetBlockBuiltin -> ExpressionTypes.GET_BLOCK_BUILTIN
                is blang.expression.builtin.GetItemCountBuiltin -> ExpressionTypes.GET_ITEM_COUNT_BUILTIN
                is blang.expression.builtin.GetItemsBuiltin -> ExpressionTypes.GET_ITEMS_BUILTIN
                is blang.expression.builtin.ItemBuiltin -> ExpressionTypes.ITEM_BUILTIN
                is blang.expression.builtin.PlaceBlockBuiltin -> ExpressionTypes.PLACE_BLOCK_BUILTIN
                is blang.expression.builtin.ReadItemCountBuiltin -> ExpressionTypes.READ_ITEM_COUNT_BUILTIN
                is blang.expression.builtin.TagBuiltin -> ExpressionTypes.TAG_BUILTIN
                is blang.expression.builtin.TagsBuiltin -> ExpressionTypes.TAGS_BUILTIN
                is blang.expression.builtin.UseItemBuiltin -> ExpressionTypes.USE_ITEM_BUILTIN
                else -> throw Exception("Expression type not found")
            }
        }

        val REGISTRY: Registry<ExpressionType<*>> = SimpleRegistry(
            RegistryKey.ofRegistry(Identifier.of("blogic", "expression_types")), Lifecycle.stable(),
        )

        val CODEC: Codec<Expression> = REGISTRY.getCodec().dispatch("type", type, ExpressionType<*>::codec)
    }
}
