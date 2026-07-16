package blang.expression.value

import blang.codec.value.ValueTypes
import net.minecraft.world.level.block.Block
import program.expression.value.Value

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
