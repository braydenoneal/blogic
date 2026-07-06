package com.braydenoneal.blang.wrapper.expression.value

import com.braydenoneal.blang.wrapper.codec.value.ValueTypes
import net.minecraft.block.Block
import parser.expression.value.Value

class BlockValue(value: Block) : Value<Block>(value) {
    override fun equals(other: Any?): Boolean {
        if (other is BlockValue) {
            return value == other.value
        }

        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + ValueTypes.BLOCK.hashCode()
        return result
    }
}
