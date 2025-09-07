package com.braydenoneal.blang.parser.expression.value

import com.braydenoneal.blang.parser.RunException
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.Item
import net.minecraft.registry.Registries

class ItemValue(value: Item) : Value<Item>(value) {
    override val valueType: ValueType<*> get() = ValueTypes.ITEM

    override fun equals(other: Any?): Boolean {
        try {
            if (other is ItemValue) {
                return value == other.value
            }
        } catch (_: Error) {
            throw RunException("Cannot equate item values outside of the game")
        }

        return false
    }

    companion object {
        val CODEC: MapCodec<ItemValue> = RecordCodecBuilder.mapCodec {
            it.group(
                Registries.ITEM.getCodec().fieldOf("value").forGetter(ItemValue::value)
            ).apply(it, ::ItemValue)
        }
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + valueType.hashCode()
        return result
    }
}
