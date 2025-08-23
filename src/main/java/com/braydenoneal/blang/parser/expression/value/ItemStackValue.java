package com.braydenoneal.blang.parser.expression.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;

public class ItemStackValue extends Value<ItemStack> {
    public static final MapCodec<ItemStackValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("value").forGetter(ItemStackValue::value)
    ).apply(instance, ItemStackValue::new));

    public ItemStackValue(ItemStack value) {
        super(value);
    }

    @Override
    public ValueType<?> getType() {
        return ValueTypes.ITEM_STACK;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            if (obj instanceof ItemStackValue itemValue) {
                return value().equals(itemValue.value());
            }
        } catch (Error e) {
            System.out.println("Cannot equate item stack values outside of the game");
        }

        return false;
    }
}
