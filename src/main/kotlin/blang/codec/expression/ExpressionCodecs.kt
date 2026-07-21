package blang.codec.expression

import blang.codec.Codecs.mutableListCodec
import blang.codec.Codecs.mutableMapCodec
import blang.codec.value.ValueCodecs
import blang.codec.value.ValueType
import blang.expression.builtin.*
import blang.expression.builtin.PrintBuiltin
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec
import program.expression.*
import program.expression.builtin.*
import program.expression.builtin.list.*
import program.expression.builtin.struct.StructEntriesBuiltin
import program.expression.builtin.struct.StructKeysBuiltin
import program.expression.builtin.struct.StructRemoveBuiltin
import program.expression.builtin.struct.StructValuesBuiltin

object ExpressionCodecs {
    val ARGUMENTS_CODEC: Codec<Arguments> = RecordCodecBuilder.create {
        it.group(
            mutableListCodec(ExpressionType.CODEC).fieldOf("nameless_arguments").forGetter(Arguments::namelessArguments),
            mutableMapCodec(Codec.STRING, ExpressionType.CODEC).fieldOf("named_arguments").forGetter(Arguments::namedArguments),
            Codec.INT.fieldOf("index").forGetter(Arguments::index),
            mutableMapCodec(Codec.STRING, ValueType.CODEC).fieldOf("computed").forGetter(Arguments::computed),
        ).apply(it, ::Arguments)
    }
    val ASSIGN_EXPRESSION_CODEC: MapCodec<AssignExpression> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("type").forGetter(AssignExpression::operator),
            ExpressionType.CODEC.fieldOf("left").forGetter(AssignExpression::left),
            ExpressionType.CODEC.fieldOf("right").forGetter(AssignExpression::right),
        ).apply(it, ::AssignExpression)
    }
    val IF_ELSE_EXPRESSION_CODEC: MapCodec<IfElseExpression> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("condition").forGetter(IfElseExpression::condition),
            ExpressionType.CODEC.fieldOf("expression_a").forGetter(IfElseExpression::expressionA),
            ExpressionType.CODEC.fieldOf("expression_b").forGetter(IfElseExpression::expressionB),
        ).apply(it, ::IfElseExpression)
    }
    val ACCESS_EXPRESSION_CODEC: MapCodec<AccessExpression> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("left").forGetter(AccessExpression::left),
            ExpressionType.CODEC.fieldOf("right").forGetter(AccessExpression::right),
        ).apply(it, ::AccessExpression)
    }
    val LIST_EXPRESSION_CODEC: MapCodec<ListExpression> = mapCodec {
        it.group(
            mutableListCodec(ExpressionType.CODEC).fieldOf("expressions").forGetter(ListExpression::expressions),
        ).apply(it, ::ListExpression)
    }
    val CALL_EXPRESSION_CODEC: MapCodec<CallExpression> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("left").forGetter(CallExpression::left),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(CallExpression::arguments),
        ).apply(it, ::CallExpression)
    }
    val DOT_EXPRESSION_CODEC: MapCodec<DotExpression> = mapCodec {
        it.group(
            ExpressionType.CODEC.fieldOf("left").forGetter(DotExpression::left),
            Codec.STRING.fieldOf("right").forGetter(DotExpression::right),
        ).apply(it, ::DotExpression)
    }
    val STRUCT_EXPRESSION_CODEC: MapCodec<StructExpression> = mapCodec {
        it.group(
            Codec.list(PairCodec.pair(Codec.STRING, ExpressionType.CODEC)).fieldOf("expressions").forGetter(StructExpression::expressions),
        ).apply(it, ::StructExpression)
    }
    val IDENTIFIER_EXPRESSION_CODEC: MapCodec<IdentifierExpression> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("name").forGetter(IdentifierExpression::name),
        ).apply(it, ::IdentifierExpression)
    }
    val BINARY_OPERATOR_EXPRESSION_CODEC: MapCodec<BinaryOperatorExpression> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("operator").forGetter(BinaryOperatorExpression::operator),
            ExpressionType.CODEC.fieldOf("left").forGetter(BinaryOperatorExpression::left),
            ExpressionType.CODEC.fieldOf("right").forGetter(BinaryOperatorExpression::right),
        ).apply(it, ::BinaryOperatorExpression)
    }
    val UNARY_OPERATOR_EXPRESSION_CODEC: MapCodec<UnaryOperatorExpression> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("operator").forGetter(UnaryOperatorExpression::operator),
            ExpressionType.CODEC.fieldOf("operand").forGetter(UnaryOperatorExpression::operand),
        ).apply(it, ::UnaryOperatorExpression)
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
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("value").forGetter(ListAppendBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListAppendBuiltin::arguments),
        ).apply(it, ::ListAppendBuiltin)
    }
    val LIST_CONTAINS_ALL_BUILTIN_CODEC: MapCodec<ListContainsAllBuiltin> = mapCodec {
        it.group(
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("value").forGetter(ListContainsAllBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListContainsAllBuiltin::arguments),
        ).apply(it, ::ListContainsAllBuiltin)
    }
    val LIST_CONTAINS_BUILTIN_CODEC: MapCodec<ListContainsBuiltin> = mapCodec {
        it.group(
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("value").forGetter(ListContainsBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListContainsBuiltin::arguments),
        ).apply(it, ::ListContainsBuiltin)
    }
    val LIST_INSERT_BUILTIN_CODEC: MapCodec<ListInsertBuiltin> = mapCodec {
        it.group(
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("value").forGetter(ListInsertBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListInsertBuiltin::arguments),
        ).apply(it, ::ListInsertBuiltin)
    }
    val LIST_POP_BUILTIN_CODEC: MapCodec<ListPopBuiltin> = mapCodec {
        it.group(
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("value").forGetter(ListPopBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListPopBuiltin::arguments),
        ).apply(it, ::ListPopBuiltin)
    }
    val LIST_REMOVE_BUILTIN_CODEC: MapCodec<ListRemoveBuiltin> = mapCodec {
        it.group(
            ValueCodecs.LIST_VALUE_CODEC.fieldOf("value").forGetter(ListRemoveBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(ListRemoveBuiltin::arguments),
        ).apply(it, ::ListRemoveBuiltin)
    }
    val STRUCT_ENTRIES_BUILTIN_CODEC: MapCodec<StructEntriesBuiltin> = mapCodec {
        it.group(
            ValueCodecs.STRUCT_VALUE_CODEC.fieldOf("value").forGetter(StructEntriesBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(StructEntriesBuiltin::arguments),
        ).apply(it, ::StructEntriesBuiltin)
    }
    val STRUCT_KEYS_BUILTIN_CODEC: MapCodec<StructKeysBuiltin> = mapCodec {
        it.group(
            ValueCodecs.STRUCT_VALUE_CODEC.fieldOf("value").forGetter(StructKeysBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(StructKeysBuiltin::arguments),
        ).apply(it, ::StructKeysBuiltin)
    }
    val STRUCT_REMOVE_BUILTIN_CODEC: MapCodec<StructRemoveBuiltin> = mapCodec {
        it.group(
            ValueCodecs.STRUCT_VALUE_CODEC.fieldOf("value").forGetter(StructRemoveBuiltin::value),
            ARGUMENTS_CODEC.fieldOf("arguments").forGetter(StructRemoveBuiltin::arguments),
        ).apply(it, ::StructRemoveBuiltin)
    }
    val STRUCT_VALUES_BUILTIN_CODEC: MapCodec<StructValuesBuiltin> = mapCodec {
        it.group(
            ValueCodecs.STRUCT_VALUE_CODEC.fieldOf("value").forGetter(StructValuesBuiltin::value),
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
