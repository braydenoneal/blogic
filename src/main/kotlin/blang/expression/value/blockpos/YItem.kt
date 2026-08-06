package blang.expression.value.blockpos

import program.Program
import program.expression.value.Value
import program.expression.value.ValueItem
import program.expression.value.integer.IntegerValue

object YItem : ValueItem<BlockPosValue>() {
    override val name: String = "y"

    context(program: Program, value: BlockPosValue)
    override fun get(): Value<*> {
        return IntegerValue(value.value.y)
    }
}
