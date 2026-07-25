package blang.expression.builtin

import blang.expression.value.ItemValue
import blang.expression.value.TagValue
import program.Program
import program.expression.Arguments
import program.expression.value.ListValue
import program.expression.value.Value

object TagsBuiltin {
    fun call(program: Program, arguments: Arguments): Value<*> {
        val item = arguments.get<ItemValue>(program, "value").value
        val tags = ArrayList<Value<*>>()

        item.defaultInstance.tags().forEach { tags.add(TagValue(it)) }

        return ListValue(tags)
    }
}
