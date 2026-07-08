package blang.codec.value

import blang.codec.Codecs.FUNCT_CODEC
import blang.codec.Codecs.mutableListCodec
import blang.codec.expression.PairCodec
import blang.expression.value.BlockValue
import blang.expression.value.ItemStackValue
import blang.expression.value.ItemValue
import blang.expression.value.TagValue
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import parser.expression.value.*

object ValueCodecs {
    val NULL_CODEC: Codec<Null> = MapCodec.unitCodec(Null())
    val RANGE_CODEC: Codec<Range> = RecordCodecBuilder.create {
        it.group(
            Codec.INT.fieldOf("start").forGetter(Range::start),
            Codec.INT.fieldOf("end").forGetter(Range::end),
            Codec.INT.fieldOf("step").forGetter(Range::step),
        ).apply(it, ::Range)
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
    val FUNCTION_VALUE_CODEC: MapCodec<FunctionValue> = mapCodec {
        it.group(
            FUNCT_CODEC.fieldOf("value").forGetter(FunctionValue::value),
        ).apply(it, ::FunctionValue)
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
    val NULL_VALUE_CODEC: MapCodec<NullValue> = mapCodec {
        it.group(
            NULL_CODEC.fieldOf("null").forGetter(NullValue::value),
        ).apply(it, ::NullValue)
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
            mutableListCodec(PairCodec.pair(Codec.STRING, ValueType.CODEC)).fieldOf("value").forGetter(StructValue::value),
        ).apply(it, ::StructValue)
    }
    val BLOCK_VALUE_CODEC: MapCodec<BlockValue> = mapCodec {
        it.group(
            Registries.BLOCK.getCodec().fieldOf("value").forGetter(BlockValue::value),
        ).apply(it, ::BlockValue)
    }
    val ITEM_STACK_CODEC: MapCodec<ItemStackValue> = mapCodec {
        it.group(
            ItemStack.CODEC.fieldOf("value").forGetter(ItemStackValue::value),
        ).apply(it, ::ItemStackValue)
    }
    val ITEM_CODEC: MapCodec<ItemValue> = mapCodec {
        it.group(
            Registries.ITEM.getCodec().fieldOf("value").forGetter(ItemValue::value),
        ).apply(it, ::ItemValue)
    }
    val TAG_CODEC: MapCodec<TagValue> = mapCodec {
        it.group(
            TagKey.codec(RegistryKeys.ITEM).fieldOf("value").forGetter(TagValue::value),
        ).apply(it, ::TagValue)
    }
}
