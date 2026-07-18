package blang.codec.expression

import blang.codec.value.ValueType
import blang.expression.builtin.*
import blang.expression.builtin.PrintBuiltin
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import parser.expression.BuiltinExpressionParser
import program.expression.*
import program.expression.builtin.*
import program.expression.builtin.list.*
import program.expression.builtin.struct.StructEntriesBuiltin
import program.expression.builtin.struct.StructKeysBuiltin
import program.expression.builtin.struct.StructRemoveBuiltin
import program.expression.builtin.struct.StructValuesBuiltin
import program.expression.operator.ArithmeticOperator
import program.expression.operator.BangOperator
import program.expression.operator.BooleanOperator
import program.expression.operator.ComparisonOperator
import program.expression.value.Value

object ExpressionTypes {
    val VALUE: ExpressionType<Value<*>> = register("value", ExpressionType(ValueType.MAP_CODEC))
    val ASSIGNMENT_EXPRESSION: ExpressionType<AssignmentExpression> = register("assignment_expression", ExpressionType(ExpressionCodecs.ASSIGNMENT_EXPRESSION_CODEC))
    val CALL_EXPRESSION: ExpressionType<CallExpression> = register("call_expression", ExpressionType(ExpressionCodecs.CALL_EXPRESSION_CODEC))
    val IF_ELSE_EXPRESSION: ExpressionType<IfElseExpression> = register("if_else_expression", ExpressionType(ExpressionCodecs.IF_ELSE_EXPRESSION_CODEC))
    val LIST_EXPRESSION: ExpressionType<ListExpression> = register("list_expression", ExpressionType(ExpressionCodecs.LIST_EXPRESSION_CODEC))
    val STRUCT_EXPRESSION: ExpressionType<StructExpression> = register("struct_expression", ExpressionType(ExpressionCodecs.STRUCT_EXPRESSION_CODEC))
    val LIST_ACCESS_EXPRESSION: ExpressionType<ListAccessExpression> = register("list_access_expression", ExpressionType(ExpressionCodecs.LIST_ACCESS_EXPRESSION_CODEC))
    val DOT_EXPRESSION: ExpressionType<DotExpression> = register("member_expression", ExpressionType(ExpressionCodecs.DOT_EXPRESSION_CODEC))
    val IDENTIFIER_EXPRESSION: ExpressionType<IdentifierExpression> = register("identifier_expression", ExpressionType(ExpressionCodecs.IDENTIFIER_EXPRESSION_CODEC))
    val ARITHMETIC_OPERATOR: ExpressionType<ArithmeticOperator> = register("arithmetic_operator", ExpressionType(ExpressionCodecs.ARITHMETIC_OPERATOR_CODEC))
    val BOOLEAN_OPERATOR: ExpressionType<BooleanOperator> = register("boolean_operator", ExpressionType(ExpressionCodecs.BOOLEAN_OPERATOR_CODEC))
    val COMPARISON_OPERATOR: ExpressionType<ComparisonOperator> = register("comparison_operator", ExpressionType(ExpressionCodecs.COMPARISON_OPERATOR_CODEC))
    val UNARY_OPERATOR: ExpressionType<BangOperator> = register("unary_operator", ExpressionType(ExpressionCodecs.UNARY_OPERATOR_CODEC))
    val ABSOLUTE_VALUE_BUILTIN: ExpressionType<AbsoluteValueBuiltin> = register("absolute_value_builtin", ExpressionType(ExpressionCodecs.ABSOLUTE_VALUE_BUILTIN_CODEC))
    val INTEGER_CAST_BUILTIN: ExpressionType<IntegerCastBuiltin> = register("integer_cast_builtin", ExpressionType(ExpressionCodecs.INTEGER_CAST_BUILTIN_CODEC))
    val FLOAT_CAST_BUILTIN: ExpressionType<FloatCastBuiltin> = register("float_cast_builtin", ExpressionType(ExpressionCodecs.FLOAT_CAST_BUILTIN_CODEC))
    val STRING_CAST_BUILTIN: ExpressionType<StringCastBuiltin> = register("string_cast_builtin", ExpressionType(ExpressionCodecs.STRING_CAST_BUILTIN_CODEC))
    val ROUND_BUILTIN: ExpressionType<RoundBuiltin> = register("round_builtin", ExpressionType(ExpressionCodecs.ROUND_BUILTIN_CODEC))
    val LENGTH_BUILTIN: ExpressionType<LengthBuiltin> = register("length_builtin", ExpressionType(ExpressionCodecs.LENGTH_BUILTIN_CODEC))
    val MINIMUM_BUILTIN: ExpressionType<MinimumBuiltin> = register("minimum_builtin", ExpressionType(ExpressionCodecs.MINIMUM_BUILTIN_CODEC))
    val MAXIMUM_BUILTIN: ExpressionType<MaximumBuiltin> = register("maximum_builtin", ExpressionType(ExpressionCodecs.MAXIMUM_BUILTIN_CODEC))
    val RANGE_BUILTIN: ExpressionType<RangeBuiltin> = register("range_builtin", ExpressionType(ExpressionCodecs.RANGE_BUILTIN_CODEC))
    val LIST_APPEND_BUILTIN: ExpressionType<ListAppendBuiltin> = register("list_append_builtin", ExpressionType(ExpressionCodecs.LIST_APPEND_BUILTIN_CODEC))
    val LIST_INSERT_BUILTIN: ExpressionType<ListInsertBuiltin> = register("list_insert_builtin", ExpressionType(ExpressionCodecs.LIST_INSERT_BUILTIN_CODEC))
    val LIST_REMOVE_BUILTIN: ExpressionType<ListRemoveBuiltin> = register("list_remove_builtin", ExpressionType(ExpressionCodecs.LIST_REMOVE_BUILTIN_CODEC))
    val LIST_POP_BUILTIN: ExpressionType<ListPopBuiltin> = register("list_pop_builtin", ExpressionType(ExpressionCodecs.LIST_POP_BUILTIN_CODEC))
    val LIST_CONTAINS_BUILTIN: ExpressionType<ListContainsBuiltin> = register("list_contains_builtin", ExpressionType(ExpressionCodecs.LIST_CONTAINS_BUILTIN_CODEC))
    val LIST_CONTAINS_ALL_BUILTIN: ExpressionType<ListContainsAllBuiltin> = register("list_contains_all_builtin", ExpressionType(ExpressionCodecs.LIST_CONTAINS_ALL_BUILTIN_CODEC))
    val STRUCT_REMOVE_BUILTIN: ExpressionType<StructRemoveBuiltin> = register("struct_remove_builtin", ExpressionType(ExpressionCodecs.STRUCT_REMOVE_BUILTIN_CODEC))
    val STRUCT_KEYS_BUILTIN: ExpressionType<StructKeysBuiltin> = register("struct_keys_builtin", ExpressionType(ExpressionCodecs.STRUCT_KEYS_BUILTIN_CODEC))
    val STRUCT_VALUES_BUILTIN: ExpressionType<StructValuesBuiltin> = register("struct_values_builtin", ExpressionType(ExpressionCodecs.STRUCT_VALUES_BUILTIN_CODEC))
    val STRUCT_ENTRIES_BUILTIN: ExpressionType<StructEntriesBuiltin> = register("struct_entries_builtin", ExpressionType(ExpressionCodecs.STRUCT_ENTRIES_BUILTIN_CODEC))
    val TYPE_BUILTIN: ExpressionType<TypeBuiltin> = register("type_builtin", ExpressionType(ExpressionCodecs.TYPE_BUILTIN_CODEC))
    val WAIT_BUILTIN: ExpressionType<WaitBuiltin> = register("wait_builtin", ExpressionType(ExpressionCodecs.WAIT_BUILTIN_CODEC))
    val PRINT_BUILTIN: ExpressionType<PrintBuiltin> = register("print_builtin", ExpressionType(ExpressionCodecs.PRINT_BUILTIN_CODEC))
    val CEIL_BUILTIN: ExpressionType<CeilBuiltin> = register("ceil_builtin", ExpressionType(ExpressionCodecs.CEIL_BUILTIN_CODEC))
    val FLOOR_BUILTIN: ExpressionType<FloorBuiltin> = register("floor_builtin", ExpressionType(ExpressionCodecs.FLOOR_BUILTIN_CODEC))
    val BLOCK_BUILTIN: ExpressionType<BlockBuiltin> = register("block_builtin", ExpressionType(ExpressionCodecs.BLOCK_BUILTIN_CODEC))
    val BLOCK_ITEM_BUILTIN: ExpressionType<BlockItemBuiltin> = register("block_item_builtin", ExpressionType(ExpressionCodecs.BLOCK_ITEM_BUILTIN_CODEC))
    val BREAK_BLOCK_BUILTIN: ExpressionType<BreakBlockBuiltin> = register("break_block_builtin", ExpressionType(ExpressionCodecs.BREAK_BLOCK_BUILTIN_CODEC))
    val DELETE_ITEMS_BUILTIN: ExpressionType<DeleteItemsBuiltin> = register("delete_items_builtin", ExpressionType(ExpressionCodecs.DELETE_ITEMS_BUILTIN_CODEC))
    val EXPORT_ALL_ITEMS_BUILTIN: ExpressionType<ExportAllItemsBuiltin> = register("export_all_items_builtin", ExpressionType(ExpressionCodecs.EXPORT_ALL_ITEMS_BUILTIN_CODEC))
    val GET_BLOCK_BUILTIN: ExpressionType<GetBlockBuiltin> = register("get_block_builtin", ExpressionType(ExpressionCodecs.GET_BLOCK_BUILTIN_CODEC))
    val GET_ITEM_COUNT_BUILTIN: ExpressionType<GetItemCountBuiltin> = register("get_item_count_builtin", ExpressionType(ExpressionCodecs.GET_ITEM_COUNT_BUILTIN_CODEC))
    val GET_ITEMS_BUILTIN: ExpressionType<GetItemsBuiltin> = register("get_items_builtin", ExpressionType(ExpressionCodecs.GET_ITEMS_BUILTIN_CODEC))
    val ITEM_BUILTIN: ExpressionType<ItemBuiltin> = register("item_builtin", ExpressionType(ExpressionCodecs.ITEM_BUILTIN_CODEC))
    val PLACE_BLOCK_BUILTIN: ExpressionType<PlaceBlockBuiltin> = register("place_block_builtin", ExpressionType(ExpressionCodecs.PLACE_BLOCK_BUILTIN_CODEC))
    val READ_ITEM_COUNT_BUILTIN: ExpressionType<ReadItemCountBuiltin> = register("read_item_count_builtin", ExpressionType(ExpressionCodecs.READ_ITEM_COUNT_BUILTIN_CODEC))
    val TAG_BUILTIN: ExpressionType<TagBuiltin> = register("tag_builtin", ExpressionType(ExpressionCodecs.TAG_BUILTIN_CODEC))
    val TAGS_BUILTIN: ExpressionType<TagsBuiltin> = register("tags_builtin", ExpressionType(ExpressionCodecs.TAGS_BUILTIN_CODEC))
    val USE_ITEM_BUILTIN: ExpressionType<UseItemBuiltin> = register("use_item_builtin", ExpressionType(ExpressionCodecs.USE_ITEM_BUILTIN_CODEC))

    fun <T : Expression> register(id: String, expressionType: ExpressionType<T>): ExpressionType<T> {
        return Registry.register(ExpressionType.REGISTRY, Identifier.fromNamespaceAndPath("blogic", id), expressionType)
    }

    fun initialize() {
        BuiltinExpressionParser.register("print") { arguments -> PrintBuiltin(arguments) }
        BuiltinExpressionParser.register("block") { arguments -> BlockBuiltin(arguments) }
        BuiltinExpressionParser.register("blockItem") { arguments -> BlockItemBuiltin(arguments) }
        BuiltinExpressionParser.register("breakBlock") { arguments -> BreakBlockBuiltin(arguments) }
        BuiltinExpressionParser.register("deleteItems") { arguments -> DeleteItemsBuiltin(arguments) }
        BuiltinExpressionParser.register("exportAllItems") { arguments -> ExportAllItemsBuiltin(arguments) }
        BuiltinExpressionParser.register("getBlock") { arguments -> GetBlockBuiltin(arguments) }
        BuiltinExpressionParser.register("getItemCount") { arguments -> GetItemCountBuiltin(arguments) }
        BuiltinExpressionParser.register("getItems") { arguments -> GetItemsBuiltin(arguments) }
        BuiltinExpressionParser.register("item") { arguments -> ItemBuiltin(arguments) }
        BuiltinExpressionParser.register("placeBlock") { arguments -> PlaceBlockBuiltin(arguments) }
        BuiltinExpressionParser.register("readItemCount") { arguments -> ReadItemCountBuiltin(arguments) }
        BuiltinExpressionParser.register("tag") { arguments -> TagBuiltin(arguments) }
        BuiltinExpressionParser.register("tags") { arguments -> TagsBuiltin(arguments) }
        BuiltinExpressionParser.register("useItem") { arguments -> UseItemBuiltin(arguments) }
    }
}
