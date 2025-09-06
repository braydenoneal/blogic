package com.braydenoneal.blang.parser.expression.value

import com.braydenoneal.blang.parser.RunException
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.ItemStack

class ItemStackValue(value: ItemStack) : Value<ItemStack>(value) {
    override val valueType: ValueType<*> get() = ValueTypes.ITEM_STACK

    override fun equals(other: Any?): Boolean {
        try {
            if (other is ItemStackValue) {
                return value == other.value
            }
        } catch (_: Error) {
            throw RunException("Cannot equate item stack values outside of the game")
        }

        return false
    }

    companion object {
        val CODEC: MapCodec<ItemStackValue> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                ItemStack.CODEC.fieldOf("value").forGetter(ItemStackValue::value)
            ).apply(instance, ::ItemStackValue)
        }
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + valueType.hashCode()
        return result
    }
}
