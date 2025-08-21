package com.braydenoneal.blang.parser.expression.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;

public class BlockValue extends Value<Block> {
    public static final MapCodec<BlockValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Registries.BLOCK.getCodec().fieldOf("value").forGetter(BlockValue::value)
    ).apply(instance, BlockValue::new));

    public BlockValue(Block value) {
        super(value);
    }

    @Override
    public ValueType<?> getType() {
        return ValueTypes.BLOCK;
    }

    @Override
    public String toString() {
        return "\"" + value() + "\"";
    }

    @Override
    public boolean equals(Object obj) {
        try {
            if (obj instanceof BlockValue blockValue) {
                return value().equals(blockValue.value());
            }
        } catch (Error e) {
            System.out.println("Cannot equate block values outside of the game");
        }

        return false;
    }
}
