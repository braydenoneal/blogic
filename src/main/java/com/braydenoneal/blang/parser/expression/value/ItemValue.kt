package com.braydenoneal.blang.parser.expression.value;

import com.braydenoneal.blang.parser.RunException;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

public class ItemValue extends Value<Item> {
    public static final MapCodec<ItemValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Registries.ITEM.getCodec().fieldOf("value").forGetter(ItemValue::value)
    ).apply(instance, ItemValue::new));

    public ItemValue(Item value) {
        super(value);
    }

    @Override
    public ValueType<?> getValueType() {
        return ValueTypes.ITEM;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            if (obj instanceof ItemValue itemValue) {
                return value().equals(itemValue.value());
            }
        } catch (Error e) {
            throw new RunException("Cannot equate item values outside of the game");
        }

        return false;
    }
}
