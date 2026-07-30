package blang.codec.value

import blang.codec.Codecs.FUNCTION_CODEC
import blang.codec.Codecs.STRUCT_DEFINITION_CODEC
import blang.codec.Codecs.mutableListCodec
import blang.codec.Codecs.mutableMapCodec
import blang.expression.value.BlockValue
import blang.expression.value.ItemStackValue
import blang.expression.value.ItemValue
import blang.expression.value.TagValue
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import program.expression.value.*
import program.expression.value.util.*
import java.util.*

object ValueCodecs {
    val FUNCTION_REFERENCE_CODEC: Codec<FunctionReference> = RecordCodecBuilder.create {
        it.group(
            ValueType.CODEC.optionalFieldOf("value").forGetter { functionReference -> Optional.ofNullable(functionReference.value) },
            Codec.STRING.fieldOf("name").forGetter(FunctionReference::name),
        ).apply(it) { value, name -> FunctionReference(value.orElse(null), name) }
    }
    val NULL_CODEC: Codec<Null> = MapCodec.unitCodec(Null())
    val OBJECT_CODEC: Codec<Object> = RecordCodecBuilder.create {
        it.group(
            mutableMapCodec(Codec.STRING, ValueType.CODEC).fieldOf("items").forGetter(Object::items),
            mutableMapCodec(Codec.STRING, FUNCTION_CODEC).fieldOf("functions").forGetter(Object::functions),
        ).apply(it, ::Object)
    }
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
    val FUNCTION_REFERENCE_VALUE_CODEC: MapCodec<FunctionReferenceValue> = mapCodec {
        it.group(
            FUNCTION_REFERENCE_CODEC.fieldOf("value").forGetter(FunctionReferenceValue::value),
        ).apply(it, ::FunctionReferenceValue)
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
    val OBJECT_VALUE_CODEC: MapCodec<ObjectValue> = mapCodec {
        it.group(
            OBJECT_CODEC.fieldOf("null").forGetter(ObjectValue::value),
        ).apply(it, ::ObjectValue)
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
    val TAG_CODEC: MapCodec<TagValue> = mapCodec {
        it.group(
            TagKey.hashedCodec(Registries.ITEM).fieldOf("value").forGetter(TagValue::value),
        ).apply(it, ::TagValue)
    }
}
