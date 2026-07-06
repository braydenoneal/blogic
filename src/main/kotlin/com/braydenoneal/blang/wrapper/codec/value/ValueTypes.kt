package com.braydenoneal.blang.wrapper.codec.value

import com.braydenoneal.blang.wrapper.expression.value.BlockValue
import com.braydenoneal.blang.wrapper.expression.value.ItemStackValue
import com.braydenoneal.blang.wrapper.expression.value.ItemValue
import com.braydenoneal.blang.wrapper.expression.value.TagValue
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import parser.expression.value.*

object ValueTypes {
    val BOOLEAN: ValueType<BooleanValue> = register("boolean", ValueType(ValueCodecs.BOOLEAN_VALUE_CODEC))
    val FLOAT: ValueType<FloatValue> = register("float", ValueType(ValueCodecs.FLOAT_VALUE_CODEC))
    val FUNCTION: ValueType<FunctionValue> = register("function", ValueType(ValueCodecs.FUNCTION_VALUE_CODEC))
    val INTEGER: ValueType<IntegerValue> = register("integer", ValueType(ValueCodecs.INTEGER_VALUE_CODEC))
    val LIST: ValueType<ListValue> = register("list", ValueType(ValueCodecs.LIST_VALUE_CODEC))
    val NULL: ValueType<NullValue> = register("null", ValueType(ValueCodecs.NULL_VALUE_CODEC))
    val RANGE: ValueType<RangeValue> = register("range", ValueType(ValueCodecs.RANGE_VALUE_CODEC))
    val STRING: ValueType<StringValue> = register("string", ValueType(ValueCodecs.STRING_VALUE_CODEC))
    val STRUCT: ValueType<StructValue> = register("struct", ValueType(ValueCodecs.STRUCT_VALUE_CODEC))
    val BLOCK: ValueType<BlockValue> = register("block", ValueType(ValueCodecs.BLOCK_VALUE_CODEC))
    val ITEM_STACK: ValueType<ItemStackValue> = register("item_stack", ValueType(ValueCodecs.ITEM_STACK_CODEC))
    val ITEM: ValueType<ItemValue> = register("item", ValueType(ValueCodecs.ITEM_CODEC))
    val TAG: ValueType<TagValue> = register("tag", ValueType(ValueCodecs.TAG_CODEC))

    fun <T : Value<*>> register(id: String, valueType: ValueType<T>): ValueType<T> {
        return Registry.register(ValueType.REGISTRY, Identifier.of("blogic", id), valueType)
    }

    fun initialize() {
    }
}
