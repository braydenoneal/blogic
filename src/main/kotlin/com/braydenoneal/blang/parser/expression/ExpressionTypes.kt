package com.braydenoneal.blang.parser.expression

import com.braydenoneal.blang.parser.expression.builtin.*
import com.braydenoneal.blang.parser.expression.builtin.list.*
import com.braydenoneal.blang.parser.expression.builtin.struct.StructEntriesBuiltin
import com.braydenoneal.blang.parser.expression.builtin.struct.StructKeysBuiltin
import com.braydenoneal.blang.parser.expression.builtin.struct.StructRemoveBuiltin
import com.braydenoneal.blang.parser.expression.builtin.struct.StructValuesBuiltin
import com.braydenoneal.blang.parser.expression.operator.ArithmeticOperator
import com.braydenoneal.blang.parser.expression.operator.BooleanOperator
import com.braydenoneal.blang.parser.expression.operator.ComparisonOperator
import com.braydenoneal.blang.parser.expression.operator.UnaryOperator
import com.braydenoneal.blang.parser.expression.value.Value
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ExpressionTypes {
    val VALUE: ExpressionType<Value<*>> = register("value", ExpressionType(Value.MAP_CODEC))
    val ASSIGNMENT_EXPRESSION: ExpressionType<AssignmentExpression> = register("assignment_expression", ExpressionType(AssignmentExpression.CODEC))
    val CALL_EXPRESSION: ExpressionType<CallExpression> = register("call_expression", ExpressionType(CallExpression.CODEC))
    val IF_ELSE_EXPRESSION: ExpressionType<IfElseExpression> = register("if_else_expression", ExpressionType(IfElseExpression.CODEC))
    val LIST_EXPRESSION: ExpressionType<ListExpression> = register("list_expression", ExpressionType(ListExpression.CODEC))
    val STRUCT_EXPRESSION: ExpressionType<StructExpression> = register("struct_expression", ExpressionType(StructExpression.CODEC))
    val LIST_ACCESS_EXPRESSION: ExpressionType<ListAccessExpression> = register("list_access_expression", ExpressionType(ListAccessExpression.CODEC))
    val MEMBER_EXPRESSION: ExpressionType<MemberExpression> = register("member_expression", ExpressionType(MemberExpression.CODEC))
    val MEMBER_CALL_EXPRESSION: ExpressionType<MemberCallExpression> = register("member_call_expression", ExpressionType(MemberCallExpression.CODEC))
    val NAMED_LIST_ACCESS_EXPRESSION: ExpressionType<NamedListAccessExpression> = register("named_list_access_expression", ExpressionType(NamedListAccessExpression.CODEC))
    val VARIABLE_EXPRESSION: ExpressionType<VariableExpression> = register("variable_expression", ExpressionType(VariableExpression.CODEC))
    val ARITHMETIC_OPERATOR: ExpressionType<ArithmeticOperator> = register("arithmetic_operator", ExpressionType(ArithmeticOperator.CODEC))
    val BOOLEAN_OPERATOR: ExpressionType<BooleanOperator> = register("boolean_operator", ExpressionType(BooleanOperator.CODEC))
    val COMPARISON_OPERATOR: ExpressionType<ComparisonOperator> = register("comparison_operator", ExpressionType(ComparisonOperator.CODEC))
    val UNARY_OPERATOR: ExpressionType<UnaryOperator> = register("unary_operator", ExpressionType(UnaryOperator.CODEC))
    val ABSOLUTE_VALUE_BUILTIN: ExpressionType<AbsoluteValueBuiltin> = register("absolute_value_builtin", ExpressionType(AbsoluteValueBuiltin.CODEC))
    val INTEGER_CAST_BUILTIN: ExpressionType<IntegerCastBuiltin> = register("integer_cast_builtin", ExpressionType(IntegerCastBuiltin.CODEC))
    val FLOAT_CAST_BUILTIN: ExpressionType<FloatCastBuiltin> = register("float_cast_builtin", ExpressionType(FloatCastBuiltin.CODEC))
    val STRING_CAST_BUILTIN: ExpressionType<StringCastBuiltin> = register("string_cast_builtin", ExpressionType(StringCastBuiltin.CODEC))
    val ROUND_BUILTIN: ExpressionType<RoundBuiltin> = register("round_builtin", ExpressionType(RoundBuiltin.CODEC))
    val LENGTH_BUILTIN: ExpressionType<LengthBuiltin> = register("length_builtin", ExpressionType(LengthBuiltin.CODEC))
    val BLOCK_BUILTIN: ExpressionType<BlockBuiltin> = register("block_builtin", ExpressionType(BlockBuiltin.CODEC))
    val ITEM_BUILTIN: ExpressionType<ItemBuiltin> = register("item_builtin", ExpressionType(ItemBuiltin.CODEC))
    val PRINT_BUILTIN: ExpressionType<PrintBuiltin> = register("print_builtin", ExpressionType(PrintBuiltin.CODEC))
    val GET_BLOCK_BUILTIN: ExpressionType<GetBlockBuiltin> = register("get_block_builtin", ExpressionType(GetBlockBuiltin.CODEC))
    val PLACE_BLOCK_BUILTIN: ExpressionType<PlaceBlockBuiltin> = register("place_block_builtin", ExpressionType(PlaceBlockBuiltin.CODEC))
    val BREAK_BLOCK_BUILTIN: ExpressionType<BreakBlockBuiltin> = register("break_block_builtin", ExpressionType(BreakBlockBuiltin.CODEC))
    val USE_ITEM_BUILTIN: ExpressionType<UseItemBuiltin> = register("use_item_builtin", ExpressionType(UseItemBuiltin.CODEC))
    val EXPORT_ALL_ITEMS_BUILTIN: ExpressionType<ExportAllItemsBuiltin> = register("export_all_items_builtin", ExpressionType(ExportAllItemsBuiltin.CODEC))
    val DELETE_ITEMS_BUILTIN: ExpressionType<DeleteItemsBuiltin> = register("delete_items_builtin", ExpressionType(DeleteItemsBuiltin.CODEC))
    val GET_ITEMS_BUILTIN: ExpressionType<GetItemsBuiltin> = register("get_items_builtin", ExpressionType(GetItemsBuiltin.CODEC))
    val GET_ITEM_COUNT_BUILTIN: ExpressionType<GetItemCountBuiltin> = register("get_item_count_builtin", ExpressionType(GetItemCountBuiltin.CODEC))
    val MINIMUM_BUILTIN: ExpressionType<MinimumBuiltin> = register("minimum_builtin", ExpressionType(MinimumBuiltin.CODEC))
    val MAXIMUM_BUILTIN: ExpressionType<MaximumBuiltin> = register("maximum_builtin", ExpressionType(MaximumBuiltin.CODEC))
    val RANGE_BUILTIN: ExpressionType<RangeBuiltin> = register("range_builtin", ExpressionType(RangeBuiltin.CODEC))
    val LIST_APPEND_BUILTIN: ExpressionType<ListAppendBuiltin> = register("list_append_builtin", ExpressionType(ListAppendBuiltin.CODEC))
    val LIST_INSERT_BUILTIN: ExpressionType<ListInsertBuiltin> = register("list_insert_builtin", ExpressionType(ListInsertBuiltin.CODEC))
    val LIST_REMOVE_BUILTIN: ExpressionType<ListRemoveBuiltin> = register("list_remove_builtin", ExpressionType(ListRemoveBuiltin.CODEC))
    val LIST_POP_BUILTIN: ExpressionType<ListPopBuiltin> = register("list_pop_builtin", ExpressionType(ListPopBuiltin.CODEC))
    val LIST_CONTAINS_BUILTIN: ExpressionType<ListContainsBuiltin> = register("list_contains_builtin", ExpressionType(ListContainsBuiltin.CODEC))
    val LIST_CONTAINS_ALL_BUILTIN: ExpressionType<ListContainsAllBuiltin> = register("list_contains_all_builtin", ExpressionType(ListContainsAllBuiltin.CODEC))
    val STRUCT_REMOVE_BUILTIN: ExpressionType<StructRemoveBuiltin> = register("struct_remove_builtin", ExpressionType(StructRemoveBuiltin.CODEC))
    val STRUCT_KEYS_BUILTIN: ExpressionType<StructKeysBuiltin> = register("struct_keys_builtin", ExpressionType(StructKeysBuiltin.CODEC))
    val STRUCT_VALUES_BUILTIN: ExpressionType<StructValuesBuiltin> = register("struct_values_builtin", ExpressionType(StructValuesBuiltin.CODEC))
    val STRUCT_ENTRIES_BUILTIN: ExpressionType<StructEntriesBuiltin> = register("struct_entries_builtin", ExpressionType(StructEntriesBuiltin.CODEC))
    val TYPE_BUILTIN: ExpressionType<TypeBuiltin> = register("type_builtin", ExpressionType(TypeBuiltin.CODEC))

    fun <T : Expression> register(id: String, expressionType: ExpressionType<T>): ExpressionType<T> {
        return Registry.register(ExpressionType.REGISTRY, Identifier.of("blogic", id), expressionType)
    }

    fun initialize() {
    }
}
