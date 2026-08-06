package blang.expression.value.blockpos

import program.Program
import program.expression.Arguments
import program.expression.value.Value
import program.expression.value.ValueFunction
import program.expression.value.integer.IntegerValue

object OffsetFunction : ValueFunction<BlockPosValue>() {
    override val name: String = "offset"

    context(program: Program, arguments: Arguments, value: BlockPosValue)
    override fun call(): Value<*> {
        val x = get<IntegerValue>("x").value
        val y = get<IntegerValue>("y").value
        val z = get<IntegerValue>("z").value
        return BlockPosValue(value.value.offset(x, y, z))
    }
}
