package com.braydenoneal.blang.parser.expression.value;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ValueTypes {
    public static final ValueType<IntegerValue> INTEGER = register("integer", new ValueType<>(IntegerValue.CODEC));
    public static final ValueType<BooleanValue> BOOLEAN = register("boolean", new ValueType<>(BooleanValue.CODEC));
    public static final ValueType<FloatValue> FLOAT = register("float", new ValueType<>(FloatValue.CODEC));
    public static final ValueType<ListValue> LIST = register("list", new ValueType<>(ListValue.CODEC));
    public static final ValueType<RangeValue> RANGE = register("range", new ValueType<>(RangeValue.CODEC));
    public static final ValueType<StringValue> STRING = register("string", new ValueType<>(StringValue.CODEC));
    public static final ValueType<BlockValue> BLOCK = register("block", new ValueType<>(BlockValue.CODEC));
    public static final ValueType<ItemValue> ITEM = register("item", new ValueType<>(ItemValue.CODEC));
    public static final ValueType<ItemStackValue> ITEM_STACK = register("item_stack", new ValueType<>(ItemStackValue.CODEC));
    public static final ValueType<NullValue> NULL = register("null", new ValueType<>(NullValue.CODEC));
    public static final ValueType<FunctionValue> FUNCTION = register("function", new ValueType<>(FunctionValue.CODEC));

    public static <T extends Value<?>> ValueType<T> register(String id, ValueType<T> valueType) {
        return Registry.register(ValueType.REGISTRY, Identifier.of("blogic", id), valueType);
    }

    public static void initialize() {
    }
}
