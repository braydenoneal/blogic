package com.braydenoneal.blang.parser.expression.value

import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ValueTypes {
    val INTEGER: ValueType<IntegerValue> = register("integer", ValueType(IntegerValue.CODEC))
    val BOOLEAN: ValueType<BooleanValue> = register("boolean", ValueType(BooleanValue.CODEC))
    val FLOAT: ValueType<FloatValue> = register("float", ValueType(FloatValue.CODEC))
    val LIST: ValueType<ListValue> = register("list", ValueType(ListValue.CODEC))
    val RANGE: ValueType<RangeValue> = register("range", ValueType(RangeValue.CODEC))
    val STRING: ValueType<StringValue> = register("string", ValueType(StringValue.CODEC))
    val BLOCK: ValueType<BlockValue> = register("block", ValueType(BlockValue.CODEC))
    val ITEM: ValueType<ItemValue> = register("item", ValueType(ItemValue.CODEC))
    val ITEM_STACK: ValueType<ItemStackValue> = register("item_stack", ValueType(ItemStackValue.CODEC))
    val TAG: ValueType<TagValue> = register("tag", ValueType(TagValue.CODEC))
    val NULL: ValueType<NullValue> = register("null", ValueType(NullValue.CODEC))
    val FUNCTION: ValueType<FunctionValue> = register("function", ValueType(FunctionValue.CODEC))
    val STRUCT: ValueType<StructValue> = register("struct", ValueType(StructValue.CODEC))

    fun <T : Value<*>> register(id: String, valueType: ValueType<T>): ValueType<T> {
        return Registry.register(ValueType.REGISTRY, Identifier.of("blogic", id), valueType)
    }

    fun initialize() {
    }
}
