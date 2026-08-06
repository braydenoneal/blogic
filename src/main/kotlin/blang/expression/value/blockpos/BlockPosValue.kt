package blang.expression.value.blockpos

import blang.codec.value.ValueCodecs
import net.minecraft.core.BlockPos
import program.Program
import program.expression.Arguments
import program.expression.value.Static
import program.expression.value.Value
import program.expression.value.get
import program.expression.value.integer.IntegerValue

class BlockPosValue(value: BlockPos) : Value<BlockPos>(value) {
    override fun equals(other: Any?): Boolean {
        return other is BlockPosValue && value == other.value
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + ValueCodecs.BLOCK_VALUE_CODEC.hashCode()
    }

    context(program: Program)
    override fun getItem(name: String): Value<*> {
        return static.items[name]?.get() ?: super.getItem(name)
    }

    context(program: Program, arguments: Arguments)
    override fun innerCallFunction(name: String, local: Boolean): Value<*> {
        return static.functions[name]?.call() ?: super.innerCallFunction(name, local)
    }

    override val static = Companion

    companion object : Static<BlockPosValue>() {
        override val name = "BlockPos"

        context(program: Program, arguments: Arguments)
        override fun innerCall(): Value<*> {
            val x = get<IntegerValue>("x").value
            val y = get<IntegerValue>("y").value
            val z = get<IntegerValue>("z").value
            return BlockPosValue(BlockPos(x, y, z))
        }

        override fun initializeItems() {
            register(XItem)
            register(YItem)
            register(ZItem)
        }

        override fun initializeFunctions() {
            register(OffsetFunction)
        }
    }
}
