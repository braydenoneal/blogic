package blang.expression.value.item

import blang.expression.value.itemtag.ItemTagValue
import program.Program
import program.expression.Arguments
import program.expression.value.Value
import program.expression.value.ValueFunction
import program.expression.value.list.ListValue

object TagsFunction : ValueFunction<ItemValue>() {
    override val name: String = "tags"

    context(program: Program, arguments: Arguments, value: ItemValue)
    override fun call(): Value<*> {
        return ListValue(value.value.defaultInstance.tags().map { ItemTagValue(it) }.toList().toMutableList())
    }
}
