package com.braydenoneal.blang.parser.expression.value

import com.braydenoneal.blang.parser.RunException
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

class TagValue(value: TagKey<Item>) : Value<TagKey<Item>>(value) {
    override val valueType: ValueType<*> get() = ValueTypes.TAG

    override fun equals(other: Any?): Boolean {
        try {
            if (other is TagValue) {
                return value == other.value
            }
        } catch (_: Error) {
            throw RunException("Cannot equate tag values outside of the game")
        }

        return false
    }

    companion object {
        val CODEC: MapCodec<TagValue> = RecordCodecBuilder.mapCodec {
            it.group(
                TagKey.codec(RegistryKeys.ITEM).fieldOf("value").forGetter(TagValue::value)
            ).apply(it, ::TagValue)
        }
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + valueType.hashCode()
        return result
    }
}
