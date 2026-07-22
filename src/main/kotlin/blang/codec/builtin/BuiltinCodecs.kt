package blang.codec.builtin

import blang.codec.expression.ExpressionCodecs.ARGUMENTS_CODEC
import blang.expression.builtin.*
import blang.expression.builtin.PrintBuiltin
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec
import program.expression.builtin.*

object BuiltinCodecs {
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
