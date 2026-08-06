package blang.expression.value.block

import blang.expression.value.blocktag.BlockTagValue
import program.Program
import program.expression.Arguments
import program.expression.value.Value
import program.expression.value.ValueFunction
import program.expression.value.list.ListValue

object TagsFunction : ValueFunction<BlockValue>() {
    override val name: String = "tags"

    context(program: Program, arguments: Arguments, value: BlockValue)
    override fun call(): Value<*> {
        return ListValue(value.value.defaultBlockState().tags().map { BlockTagValue(it) }.toList().toMutableList())
    }
}
