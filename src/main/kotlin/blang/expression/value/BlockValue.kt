package blang.expression.value

import net.minecraft.world.level.block.Block
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
        result = 31 * result + blang.codec.value.ValueTypes.BLOCK.hashCode()
        return result
    }
}
