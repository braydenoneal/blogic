package blang.expression.value

import blang.codec.value.ValueCodecs
import net.minecraft.core.BlockPos
import program.Program
import program.expression.Arguments
import program.expression.value.*
import program.expression.value.Static

class BlockPosValue(value: BlockPos) : Value<BlockPos>(value) {
    override fun typeString(): String = "blockPos"

    override fun equals(other: Any?): Boolean {
        return other is BlockPosValue && value == other.value
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + ValueCodecs.BLOCK_VALUE_CODEC.hashCode()
    }

    context(program: Program)
    override fun getItem(name: String): Value<*> {
        return when (name) {
            "x" -> IntegerValue(value.x)
            "y" -> IntegerValue(value.y)
            "z" -> IntegerValue(value.z)
            else -> super.getItem(name)
        }
    }

    context(program: Program, arguments: Arguments)
    override fun innerCallFunction(name: String, local: Boolean): Value<*> {
        return when (name) {
            "offset" -> offset()
            else -> super.innerCallFunction(name, local)
        }
    }

    context(program: Program, arguments: Arguments)
    fun offset(): Value<*> {
        val x = get<IntegerValue>("x").value
        val y = get<IntegerValue>("y").value
        val z = get<IntegerValue>("z").value
        return BlockPosValue(value.offset(x, y, z))
    }

    companion object : Static {
        override val name: String = "BlockPos"

        context(program: Program, arguments: Arguments)
        override fun innerCall(): Value<*> {
            val x = get<IntegerValue>("x").value
            val y = get<IntegerValue>("y").value
            val z = get<IntegerValue>("z").value
            return BlockPosValue(BlockPos(x, y, z))
        }
    }
}
