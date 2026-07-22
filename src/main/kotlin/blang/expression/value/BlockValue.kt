package blang.expression.value

import blang.codec.value.ValueCodecs
import net.minecraft.world.level.block.Block
import program.expression.value.Value

class BlockValue(value: Block) : Value<Block>(value) {
    override fun typeString(): String = "block"

    override fun equals(other: Any?): Boolean {
        return other is BlockValue && value == other.value
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + ValueCodecs.BLOCK_VALUE_CODEC.hashCode()
        return result
    }
}
