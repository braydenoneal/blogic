package blang.expression.value.blockpos

import program.Program
import program.expression.value.Value
import program.expression.value.ValueItem
import program.expression.value.integer.IntegerValue

object ZItem : ValueItem<BlockPosValue>() {
    override val name: String = "z"

    context(program: Program, value: BlockPosValue)
    override fun get(): Value<*> {
        return IntegerValue(value.value.z)
    }
}
