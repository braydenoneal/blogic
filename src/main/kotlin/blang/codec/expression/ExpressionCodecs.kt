package blang.codec.expression

import blang.codec.Codecs.mutableListCodec
import blang.codec.Codecs.mutableMapCodec
import blang.codec.value.ValueCodecs
import blang.expression.builtin.*
import blang.expression.builtin.PrintBuiltin
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec
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

object ExpressionCodecs {
    val ARGUMENTS_CODEC: Codec<Arguments> = RecordCodecBuilder.create {
        it.group(
            mutableListCodec(ExpressionType.CODEC).fieldOf("nameless_arguments").forGetter(Arguments::namelessArguments),
            mutableMapCodec(Codec.STRING, ExpressionType.CODEC).fieldOf("named_arguments").forGetter(Arguments::namedArguments),
        ).apply(it, ::Arguments)
    }
    val ASSIGNMENT_EXPRESSION_CODEC: MapCodec<AssignmentExpression> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("type").forGetter(AssignmentExpression::operator),
            ExpressionType.CODEC.fieldOf("variable_expression").forGetter(AssignmentExpression::variableExpression),
            ExpressionType.CODEC.fieldOf("expression").forGetter(AssignmentExpression::variableExpression),
        ).apply(it, ::AssignmentExpression)
    }
    val IF_ELSE_EXPRESSION_CODEC: MapCodec<IfElseExpression> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("condition").forGetter(IfElseExpression::condition),
            ExpressionType.CODEC.fieldOf("expression_a").forGetter(IfElseExpression::expressionA),
            ExpressionType.CODEC.fieldOf("expression_b").forGetter(IfElseExpression::expressionB),
        ).apply(it, ::IfElseExpression)
    }
    val LIST_ACCESS_EXPRESSION_CODEC: MapCodec<ListAccessExpression> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("list_expression").forGetter(ListAccessExpression::listExpression),
            mutableListCodec(ExpressionType.CODEC).fieldOf("indices").forGetter(ListAccessExpression::indices),
        ).apply(it, ::ListAccessExpression)
    }
    val LIST_EXPRESSION_CODEC: MapCodec<ListExpression> = mapCodec {
        it.group(
            mutableListCodec(ExpressionType.CODEC).fieldOf("expressions").forGetter(ListExpression::expressions),
        ).apply(it, ::ListExpression)
    }
    val MEMBER_CALL_EXPRESSION_CODEC: MapCodec<MemberCallExpression> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("member").forGetter(MemberCallExpression::member),
            Codec.STRING.fieldOf("function_name").forGetter(MemberCallExpression::functionName),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(MemberCallExpression::arguments),
        ).apply(it, ::MemberCallExpression)
    }
    val CALL_EXPRESSION_CODEC: MapCodec<CallExpression> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("name").forGetter(CallExpression::name),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(CallExpression::arguments),
        ).apply(it, ::CallExpression)
    }
    val MEMBER_EXPRESSION_CODEC: MapCodec<MemberExpression> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("member").forGetter(MemberExpression::member),
            Codec.STRING.fieldOf("property").forGetter(MemberExpression::property),
        ).apply(it, ::MemberExpression)
    }
    val STRUCT_EXPRESSION_CODEC: MapCodec<StructExpression> = mapCodec {
        it.group(
            Codec.list(PairCodec.pair(Codec.STRING, ExpressionType.CODEC)).fieldOf("expressions").forGetter(StructExpression::expressions),
        ).apply(it, ::StructExpression)
    }
    val VARIABLE_EXPRESSION_CODEC: MapCodec<VariableExpression> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("name").forGetter(VariableExpression::name),
        ).apply(it, ::VariableExpression)
    }
    val ARITHMETIC_OPERATOR_CODEC: MapCodec<ArithmeticOperator> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("operator").forGetter(ArithmeticOperator::operator),
            ExpressionType.CODEC.fieldOf("operand_a").forGetter(ArithmeticOperator::operandA),
            ExpressionType.CODEC.fieldOf("operand_b").forGetter(ArithmeticOperator::operandB),
        ).apply(it, ::ArithmeticOperator)
    }
    val BOOLEAN_OPERATOR_CODEC: MapCodec<BooleanOperator> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("operator").forGetter(BooleanOperator::operator),
            ExpressionType.CODEC.fieldOf("operand_a").forGetter(BooleanOperator::operandA),
            ExpressionType.CODEC.fieldOf("operand_b").forGetter(BooleanOperator::operandB),
        ).apply(it, ::BooleanOperator)
    }
    val COMPARISON_OPERATOR_CODEC: MapCodec<ComparisonOperator> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("operator").forGetter(ComparisonOperator::operator),
            ExpressionType.CODEC.fieldOf("operand_a").forGetter(ComparisonOperator::operandA),
            ExpressionType.CODEC.fieldOf("operand_b").forGetter(ComparisonOperator::operandB),
        ).apply(it, ::ComparisonOperator)
    }
    val UNARY_OPERATOR_CODEC: MapCodec<BangOperator> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("operand").forGetter(BangOperator::operand),
        ).apply(it, ::BangOperator)
    }
    val ABSOLUTE_VALUE_BUILTIN_CODEC: MapCodec<AbsoluteValueBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(AbsoluteValueBuiltin::arguments),
        ).apply(it, ::AbsoluteValueBuiltin)
    }
    val CEIL_BUILTIN_CODEC: MapCodec<CeilBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(CeilBuiltin::arguments),
        ).apply(it, ::CeilBuiltin)
    }
    val FLOAT_CAST_BUILTIN_CODEC: MapCodec<FloatCastBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(FloatCastBuiltin::arguments),
        ).apply(it, ::FloatCastBuiltin)
    }
    val FLOOR_BUILTIN_CODEC: MapCodec<FloorBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(FloorBuiltin::arguments),
        ).apply(it, ::FloorBuiltin)
    }
    val INTEGER_CAST_BUILTIN_CODEC: MapCodec<IntegerCastBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(IntegerCastBuiltin::arguments),
        ).apply(it, ::IntegerCastBuiltin)
    }
    val LENGTH_BUILTIN_CODEC: MapCodec<LengthBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(LengthBuiltin::arguments),
        ).apply(it, ::LengthBuiltin)
    }
    val MAXIMUM_BUILTIN_CODEC: MapCodec<MaximumBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(MaximumBuiltin::arguments),
        ).apply(it, ::MaximumBuiltin)
    }
    val MINIMUM_BUILTIN_CODEC: MapCodec<MinimumBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(MinimumBuiltin::arguments),
        ).apply(it, ::MinimumBuiltin)
    }
    val PRINT_BUILTIN_CODEC: MapCodec<PrintBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(PrintBuiltin::arguments),
        ).apply(it, ::PrintBuiltin)
    }
    val RANGE_BUILTIN_CODEC: MapCodec<RangeBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(RangeBuiltin::arguments),
        ).apply(it, ::RangeBuiltin)
    }
    val ROUND_BUILTIN_CODEC: MapCodec<RoundBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(RoundBuiltin::arguments),
        ).apply(it, ::RoundBuiltin)
    }
    val STRING_CAST_BUILTIN_CODEC: MapCodec<StringCastBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(StringCastBuiltin::arguments),
        ).apply(it, ::StringCastBuiltin)
    }
    val TYPE_BUILTIN_CODEC: MapCodec<TypeBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(TypeBuiltin::arguments),
        ).apply(it, ::TypeBuiltin)
    }
    val WAIT_BUILTIN_CODEC: MapCodec<WaitBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(WaitBuiltin::arguments),
        ).apply(it, ::WaitBuiltin)
    }
    val LIST_APPEND_BUILTIN_CODEC: MapCodec<ListAppendBuiltin> = mapCodec {
        it.group(
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("list_value").forGetter(ListAppendBuiltin::listValue),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListAppendBuiltin::arguments),
        ).apply(it, ::ListAppendBuiltin)
    }
    val LIST_CONTAINS_ALL_BUILTIN_CODEC: MapCodec<ListContainsAllBuiltin> = mapCodec {
        it.group(
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("list_value").forGetter(ListContainsAllBuiltin::listValue),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListContainsAllBuiltin::arguments),
        ).apply(it, ::ListContainsAllBuiltin)
    }
    val LIST_CONTAINS_BUILTIN_CODEC: MapCodec<ListContainsBuiltin> = mapCodec {
        it.group(
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("list_value").forGetter(ListContainsBuiltin::listValue),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListContainsBuiltin::arguments),
        ).apply(it, ::ListContainsBuiltin)
    }
    val LIST_INSERT_BUILTIN_CODEC: MapCodec<ListInsertBuiltin> = mapCodec {
        it.group(
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("list_value").forGetter(ListInsertBuiltin::listValue),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListInsertBuiltin::arguments),
        ).apply(it, ::ListInsertBuiltin)
    }
    val LIST_POP_BUILTIN_CODEC: MapCodec<ListPopBuiltin> = mapCodec {
        it.group(
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("list_value").forGetter(ListPopBuiltin::listValue),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListPopBuiltin::arguments),
        ).apply(it, ::ListPopBuiltin)
    }
    val LIST_REMOVE_BUILTIN_CODEC: MapCodec<ListRemoveBuiltin> = mapCodec {
        it.group(
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("list_value").forGetter(ListRemoveBuiltin::listValue),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListRemoveBuiltin::arguments),
        ).apply(it, ::ListRemoveBuiltin)
    }
    val STRUCT_ENTRIES_BUILTIN_CODEC: MapCodec<StructEntriesBuiltin> = mapCodec {
        it.group(
            ValueCodecs.STRUCT_VALUE_CODEC.fieldOf("struct").forGetter(StructEntriesBuiltin::struct),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(StructEntriesBuiltin::arguments),
        ).apply(it, ::StructEntriesBuiltin)
    }
    val STRUCT_KEYS_BUILTIN_CODEC: MapCodec<StructKeysBuiltin> = mapCodec {
        it.group(
            ValueCodecs.STRUCT_VALUE_CODEC.fieldOf("struct").forGetter(StructKeysBuiltin::struct),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(StructKeysBuiltin::arguments),
        ).apply(it, ::StructKeysBuiltin)
    }
    val STRUCT_REMOVE_BUILTIN_CODEC: MapCodec<StructRemoveBuiltin> = mapCodec {
        it.group(
            ValueCodecs.STRUCT_VALUE_CODEC.fieldOf("struct").forGetter(StructRemoveBuiltin::struct),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(StructRemoveBuiltin::arguments),
        ).apply(it, ::StructRemoveBuiltin)
    }
    val STRUCT_VALUES_BUILTIN_CODEC: MapCodec<StructValuesBuiltin> = mapCodec {
        it.group(
            ValueCodecs.STRUCT_VALUE_CODEC.fieldOf("struct").forGetter(StructValuesBuiltin::struct),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(StructValuesBuiltin::arguments),
        ).apply(it, ::StructValuesBuiltin)
    }
    val BLOCK_BUILTIN_CODEC: MapCodec<BlockBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(BlockBuiltin::arguments),
        ).apply(it, ::BlockBuiltin)
    }
    val BLOCK_ITEM_BUILTIN_CODEC: MapCodec<BlockItemBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(BlockItemBuiltin::arguments),
        ).apply(it, ::BlockItemBuiltin)
    }
    val BREAK_BLOCK_BUILTIN_CODEC: MapCodec<BreakBlockBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(BreakBlockBuiltin::arguments),
        ).apply(it, ::BreakBlockBuiltin)
    }
    val DELETE_ITEMS_BUILTIN_CODEC: MapCodec<DeleteItemsBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(DeleteItemsBuiltin::arguments),
        ).apply(it, ::DeleteItemsBuiltin)
    }
    val EXPORT_ALL_ITEMS_BUILTIN_CODEC: MapCodec<ExportAllItemsBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ExportAllItemsBuiltin::arguments),
        ).apply(it, ::ExportAllItemsBuiltin)
    }
    val GET_BLOCK_BUILTIN_CODEC: MapCodec<GetBlockBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(GetBlockBuiltin::arguments),
        ).apply(it, ::GetBlockBuiltin)
    }
    val GET_ITEM_COUNT_BUILTIN_CODEC: MapCodec<GetItemCountBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(GetItemCountBuiltin::arguments),
        ).apply(it, ::GetItemCountBuiltin)
    }
    val GET_ITEMS_BUILTIN_CODEC: MapCodec<GetItemsBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(GetItemsBuiltin::arguments),
        ).apply(it, ::GetItemsBuiltin)
    }
    val ITEM_BUILTIN_CODEC: MapCodec<ItemBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ItemBuiltin::arguments),
        ).apply(it, ::ItemBuiltin)
    }
    val PLACE_BLOCK_BUILTIN_CODEC: MapCodec<PlaceBlockBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(PlaceBlockBuiltin::arguments),
        ).apply(it, ::PlaceBlockBuiltin)
    }
    val READ_ITEM_COUNT_BUILTIN_CODEC: MapCodec<ReadItemCountBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ReadItemCountBuiltin::arguments),
        ).apply(it, ::ReadItemCountBuiltin)
    }
    val TAG_BUILTIN_CODEC: MapCodec<TagBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(TagBuiltin::arguments),
        ).apply(it, ::TagBuiltin)
    }
    val TAGS_BUILTIN_CODEC: MapCodec<TagsBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(TagsBuiltin::arguments),
        ).apply(it, ::TagsBuiltin)
    }
    val USE_ITEM_BUILTIN_CODEC: MapCodec<UseItemBuiltin> = mapCodec {
        it.group(
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(UseItemBuiltin::arguments),
        ).apply(it, ::UseItemBuiltin)
    }
}
