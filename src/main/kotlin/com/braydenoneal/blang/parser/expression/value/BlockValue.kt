package com.braydenoneal.blang.parser.expression.value

import com.braydenoneal.blang.parser.RunException
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.registry.Registries

class BlockValue(value: Block) : Value<Block>(value) {
    override val valueType: ValueType<*> get() = ValueTypes.BLOCK

    override fun equals(other: Any?): Boolean {
        try {
            if (other is BlockValue) {
                return value == other.value
            }
        } catch (_: Error) {
            throw RunException("Cannot equate block values outside of the game")
        }

        return false
    }

    companion object {
        val CODEC: MapCodec<BlockValue> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Registries.BLOCK.getCodec().fieldOf("value").forGetter<BlockValue>(BlockValue::value)
            ).apply(instance, ::BlockValue)
        }
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + valueType.hashCode()
        return result
    }
}
