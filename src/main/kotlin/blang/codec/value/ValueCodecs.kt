package blang.codec.value

import blang.codec.Codecs.STRUCT_DEFINITION_CODEC
import blang.codec.Codecs.mutableListCodec
import blang.codec.Codecs.mutableMapCodec
import blang.codec.expression.PairCodec.Companion.pairCodec
import blang.expression.value.block.BlockValue
import blang.expression.value.blockpos.BlockPosValue
import blang.expression.value.blocktag.BlockTagValue
import blang.expression.value.item.ItemValue
import blang.expression.value.itemstack.ItemStackValue
import blang.expression.value.itemtag.ItemTagValue
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import program.expression.value.booleanvalue.BooleanValue
import program.expression.value.floatvalue.FloatValue
import program.expression.value.identifier.IdentifierValue
import program.expression.value.integer.IntegerValue
import program.expression.value.list.ListValue
import program.expression.value.map.MapValue
import program.expression.value.nullvalue.Null
import program.expression.value.nullvalue.NullValue
import program.expression.value.pair.PairValue
import program.expression.value.range.Range
import program.expression.value.range.RangeValue
import program.expression.value.string.StringValue
import program.expression.value.struct.Struct
import program.expression.value.struct.StructValue

object ValueCodecs {
    val NULL_CODEC: Codec<Null> = MapCodec.unitCodec(Null())
    val RANGE_CODEC: Codec<Range> = RecordCodecBuilder.create {
        it.group(
            Codec.INT.fieldOf("start").forGetter(Range::start),
            Codec.INT.fieldOf("end").forGetter(Range::end),
            Codec.INT.fieldOf("step").forGetter(Range::step),
        ).apply(it, ::Range)
    }
    val STRUCT_CODEC: Codec<Struct> = RecordCodecBuilder.create {
        it.group(
            STRUCT_DEFINITION_CODEC.fieldOf("definition").forGetter(Struct::definition),
            mutableMapCodec(Codec.STRING, ValueType.CODEC).fieldOf("staticVariables").forGetter(Struct::variables),
        ).apply(it, ::Struct)
    }
    val BOOLEAN_VALUE_CODEC: MapCodec<BooleanValue> = mapCodec {
        it.group(
            Codec.BOOL.fieldOf("value").forGetter(BooleanValue::value),
        ).apply(it, ::BooleanValue)
    }
    val FLOAT_VALUE_CODEC: MapCodec<FloatValue> = mapCodec {
        it.group(
            Codec.FLOAT.fieldOf("value").forGetter(FloatValue::value),
        ).apply(it, ::FloatValue)
    }
    val IDENTIFIER_VALUE_CODEC: MapCodec<IdentifierValue> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("value").forGetter(IdentifierValue::value),
        ).apply(it, ::IdentifierValue)
    }
    val INTEGER_VALUE_CODEC: MapCodec<IntegerValue> = mapCodec {
        it.group(
            Codec.INT.fieldOf("value").forGetter(IntegerValue::value),
        ).apply(it, ::IntegerValue)
    }
    val LIST_VALUE_CODEC: MapCodec<ListValue> = mapCodec {
        it.group(
            mutableListCodec(ValueType.CODEC).fieldOf("value").forGetter(ListValue::value),
        ).apply(it, ::ListValue)
    }
    val MAP_VALUE_CODEC: MapCodec<MapValue> = mapCodec {
        it.group(
            mutableMapCodec(ValueType.CODEC, ValueType.CODEC).fieldOf("value").forGetter(MapValue::value),
        ).apply(it, ::MapValue)
    }
    val NULL_VALUE_CODEC: MapCodec<NullValue> = mapCodec {
        it.group(
            NULL_CODEC.fieldOf("null").forGetter(NullValue::value),
        ).apply(it, ::NullValue)
    }
    val PAIR_VALUE_CODEC: MapCodec<PairValue> = mapCodec {
        it.group(
            pairCodec(ValueType.CODEC, ValueType.CODEC).fieldOf("value").forGetter(PairValue::value),
        ).apply(it, ::PairValue)
    }
    val RANGE_VALUE_CODEC: MapCodec<RangeValue> = mapCodec {
        it.group(
            RANGE_CODEC.fieldOf("value").forGetter(RangeValue::value),
        ).apply(it, ::RangeValue)
    }
    val STRING_VALUE_CODEC: MapCodec<StringValue> = mapCodec {
        it.group(
            Codec.STRING.fieldOf("value").forGetter(StringValue::value),
        ).apply(it, ::StringValue)
    }
    val STRUCT_VALUE_CODEC: MapCodec<StructValue> = mapCodec {
        it.group(
            STRUCT_CODEC.fieldOf("value").forGetter(StructValue::value),
        ).apply(it, ::StructValue)
    }
    val BLOCK_POS_VALUE_CODEC: MapCodec<BlockPosValue> = mapCodec {
        it.group(
            BlockPos.CODEC.fieldOf("value").forGetter(BlockPosValue::value),
        ).apply(it, ::BlockPosValue)
    }
    val BLOCK_VALUE_CODEC: MapCodec<BlockValue> = mapCodec {
        it.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("value").forGetter(BlockValue::value),
        ).apply(it, ::BlockValue)
    }
    val ITEM_STACK_CODEC: MapCodec<ItemStackValue> = mapCodec {
        it.group(
            ItemStack.CODEC.fieldOf("value").forGetter(ItemStackValue::value),
        ).apply(it, ::ItemStackValue)
    }
    val ITEM_CODEC: MapCodec<ItemValue> = mapCodec {
        it.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("value").forGetter(ItemValue::value),
        ).apply(it, ::ItemValue)
    }
    val ITEM_TAG_CODEC: MapCodec<ItemTagValue> = mapCodec {
        it.group(
            TagKey.hashedCodec(Registries.ITEM).fieldOf("value").forGetter(ItemTagValue::value),
        ).apply(it, ::ItemTagValue)
    }
    val BLOCK_TAG_CODEC: MapCodec<BlockTagValue> = mapCodec {
        it.group(
            TagKey.hashedCodec(Registries.BLOCK).fieldOf("value").forGetter(BlockTagValue::value),
        ).apply(it, ::BlockTagValue)
    }
}
