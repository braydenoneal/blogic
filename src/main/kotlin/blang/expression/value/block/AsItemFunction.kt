package blang.expression.value.block

import blang.expression.value.item.ItemValue
import program.Program
import program.expression.Arguments
import program.expression.value.Value
import program.expression.value.ValueFunction

object AsItemFunction : ValueFunction<BlockValue>() {
    override val name: String = "asItem"

    context(program: Program, arguments: Arguments, value: BlockValue)
    override fun call(): Value<*> {
        return ItemValue(value.value.asItem())
    }
}
