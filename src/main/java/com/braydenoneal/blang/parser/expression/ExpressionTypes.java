package com.braydenoneal.blang.parser.expression;

import com.braydenoneal.blang.parser.expression.builtin.*;
import com.braydenoneal.blang.parser.expression.builtin.list.*;
import com.braydenoneal.blang.parser.expression.operator.ArithmeticOperator;
import com.braydenoneal.blang.parser.expression.operator.BooleanOperator;
import com.braydenoneal.blang.parser.expression.operator.ComparisonOperator;
import com.braydenoneal.blang.parser.expression.operator.UnaryOperator;
import com.braydenoneal.blang.parser.expression.value.Value;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ExpressionTypes {
    public static final ExpressionType<Value<?>> VALUE = register("value", new ExpressionType<>(Value.MAP_CODEC));
    public static final ExpressionType<AssignmentExpression> ASSIGNMENT_EXPRESSION = register("assignment_expression", new ExpressionType<>(AssignmentExpression.CODEC));
    public static final ExpressionType<CallExpression> CALL_EXPRESSION = register("call_expression", new ExpressionType<>(CallExpression.CODEC));
    public static final ExpressionType<IfElseExpression> IF_ELSE_EXPRESSION = register("if_else_expression", new ExpressionType<>(IfElseExpression.CODEC));
    public static final ExpressionType<ListExpression> LIST_EXPRESSION = register("list_expression", new ExpressionType<>(ListExpression.CODEC));
    public static final ExpressionType<ListAccessExpression> LIST_ACCESS_EXPRESSION = register("list_access_expression", new ExpressionType<>(ListAccessExpression.CODEC));
    public static final ExpressionType<MemberExpression> MEMBER_EXPRESSION = register("member_expression", new ExpressionType<>(MemberExpression.CODEC));
    public static final ExpressionType<MemberCallExpression> MEMBER_CALL_EXPRESSION = register("member_call_expression", new ExpressionType<>(MemberCallExpression.CODEC));
    public static final ExpressionType<NamedListAccessExpression> NAMED_LIST_ACCESS_EXPRESSION = register("named_list_access_expression", new ExpressionType<>(NamedListAccessExpression.CODEC));
    public static final ExpressionType<VariableExpression> VARIABLE_EXPRESSION = register("variable_expression", new ExpressionType<>(VariableExpression.CODEC));
    public static final ExpressionType<ArithmeticOperator> ARITHMETIC_OPERATOR = register("arithmetic_operator", new ExpressionType<>(ArithmeticOperator.CODEC));
    public static final ExpressionType<BooleanOperator> BOOLEAN_OPERATOR = register("boolean_operator", new ExpressionType<>(BooleanOperator.CODEC));
    public static final ExpressionType<ComparisonOperator> COMPARISON_OPERATOR = register("comparison_operator", new ExpressionType<>(ComparisonOperator.CODEC));
    public static final ExpressionType<UnaryOperator> UNARY_OPERATOR = register("unary_operator", new ExpressionType<>(UnaryOperator.CODEC));
    public static final ExpressionType<AbsoluteValueBuiltin> ABSOLUTE_VALUE_BUILTIN = register("absolute_value_builtin", new ExpressionType<>(AbsoluteValueBuiltin.CODEC));
    public static final ExpressionType<IntegerCastBuiltin> INTEGER_CAST_BUILTIN = register("integer_cast_builtin", new ExpressionType<>(IntegerCastBuiltin.CODEC));
    public static final ExpressionType<FloatCastBuiltin> FLOAT_CAST_BUILTIN = register("float_cast_builtin", new ExpressionType<>(FloatCastBuiltin.CODEC));
    public static final ExpressionType<StringCastBuiltin> STRING_CAST_BUILTIN = register("string_cast_builtin", new ExpressionType<>(StringCastBuiltin.CODEC));
    public static final ExpressionType<RoundBuiltin> ROUND_BUILTIN = register("round_builtin", new ExpressionType<>(RoundBuiltin.CODEC));
    public static final ExpressionType<LengthBuiltin> LENGTH_BUILTIN = register("length_builtin", new ExpressionType<>(LengthBuiltin.CODEC));
    public static final ExpressionType<BlockBuiltin> BLOCK_BUILTIN = register("block_builtin", new ExpressionType<>(BlockBuiltin.CODEC));
    public static final ExpressionType<ItemBuiltin> ITEM_BUILTIN = register("item_builtin", new ExpressionType<>(ItemBuiltin.CODEC));
    public static final ExpressionType<PrintBuiltin> PRINT_BUILTIN = register("print_builtin", new ExpressionType<>(PrintBuiltin.CODEC));
    public static final ExpressionType<GetBlockBuiltin> GET_BLOCK_BUILTIN = register("get_block_builtin", new ExpressionType<>(GetBlockBuiltin.CODEC));
    public static final ExpressionType<PlaceBlockBuiltin> PLACE_BLOCK_BUILTIN = register("place_block_builtin", new ExpressionType<>(PlaceBlockBuiltin.CODEC));
    public static final ExpressionType<BreakBlockBuiltin> BREAK_BLOCK_BUILTIN = register("break_block_builtin", new ExpressionType<>(BreakBlockBuiltin.CODEC));
    public static final ExpressionType<UseItemBuiltin> USE_ITEM_BUILTIN = register("use_item_builtin", new ExpressionType<>(UseItemBuiltin.CODEC));
    public static final ExpressionType<ExportAllItemsBuiltin> EXPORT_ALL_ITEMS_BUILTIN = register("export_all_items_builtin", new ExpressionType<>(ExportAllItemsBuiltin.CODEC));
    public static final ExpressionType<DeleteItemsBuiltin> DELETE_ITEMS_BUILTIN = register("delete_items_builtin", new ExpressionType<>(DeleteItemsBuiltin.CODEC));
    public static final ExpressionType<GetItemsBuiltin> GET_ITEMS_BUILTIN = register("get_items_builtin", new ExpressionType<>(GetItemsBuiltin.CODEC));
    public static final ExpressionType<MinimumBuiltin> MINIMUM_BUILTIN = register("minimum_builtin", new ExpressionType<>(MinimumBuiltin.CODEC));
    public static final ExpressionType<MaximumBuiltin> MAXIMUM_BUILTIN = register("maximum_builtin", new ExpressionType<>(MaximumBuiltin.CODEC));
    public static final ExpressionType<RangeBuiltin> RANGE_BUILTIN = register("range_builtin", new ExpressionType<>(RangeBuiltin.CODEC));
    public static final ExpressionType<ListAppendBuiltin> LIST_APPEND_BUILTIN = register("list_append_builtin", new ExpressionType<>(ListAppendBuiltin.CODEC));
    public static final ExpressionType<ListInsertBuiltin> LIST_INSERT_BUILTIN = register("list_insert_builtin", new ExpressionType<>(ListInsertBuiltin.CODEC));
    public static final ExpressionType<ListRemoveBuiltin> LIST_REMOVE_BUILTIN = register("list_remove_builtin", new ExpressionType<>(ListRemoveBuiltin.CODEC));
    public static final ExpressionType<ListPopBuiltin> LIST_POP_BUILTIN = register("list_pop_builtin", new ExpressionType<>(ListPopBuiltin.CODEC));
    public static final ExpressionType<ListContainsBuiltin> LIST_CONTAINS_BUILTIN = register("list_contains_builtin", new ExpressionType<>(ListContainsBuiltin.CODEC));
    public static final ExpressionType<ListContainsAllBuiltin> LIST_CONTAINS_ALL_BUILTIN = register("list_contains_all_builtin", new ExpressionType<>(ListContainsAllBuiltin.CODEC));

    public static <T extends Expression> ExpressionType<T> register(String id, ExpressionType<T> expressionType) {
        return Registry.register(ExpressionType.REGISTRY, Identifier.of("blogic", id), expressionType);
    }

    public static void initialize() {
    }
}
